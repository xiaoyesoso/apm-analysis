package com.hcloud.apm.analysis.label

import java.util.Properties

import com.alibaba.fastjson.JSON
import com.hcloud.apm.analysis.bean.{Constants, ModelParamBean, TransactionBean}
import com.hcloud.apm.analysis.common.Configuration
import com.hcloud.apm.analysis.utils.CurrentTimeUtil
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.rdd.RDD

import scala.collection.mutable.ListBuffer

//import com.hcloud.apm.anlysis.common.{AbstractModel, Configuration}
import com.hcloud.apm.analysis.common.AbstractModel
import org.apache.spark.mllib.clustering.KMeans
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.sql.SaveMode

/**
  * Created by 陈志民 on 2016/11/22.
  */
case class UserLabel(username: String, labelName: String, isLabeled: Int)

case class Result(name: String, modelName: String, feature: String, overrided: Int, transactionid: Int, dataSourceid: Int)

case class UserLabelID(name: String, id: String)

class KMeansLabelModel extends AbstractModel with Serializable {
  /**
    * 开始执行算法
    *
    * @param transaction 事务实体
    */
  override def fit(transaction: TransactionBean): Unit = {
    val srcRDD = loadDataSource(transaction);
    exeLabeling(transaction, srcRDD)

  }

  /**
    * 加载数据源
    *
    * @param transactionBean
    * @return
    */
  def loadDataSource(transactionBean: TransactionBean): RDD[String] = {
    //此处可以判断数据源类型根据不同类型加载数据源
    val fileLocation = transactionBean.getDatasource.getFileLocation
    val datasourceID = transactionBean.getDataSourceid
    val rawRDD = sc.textFile(Configuration.HDFS_DATA_PREFIX + fileLocation + "/1/" + datasourceID + ".csv", 5)
    rawRDD
  }

  /**
    * 执行打标签过程
    *
    * @param transactionBean
    * @param srcRDD
    */
  def exeLabeling(transactionBean: TransactionBean, srcRDD: RDD[String]): Unit = {
    val username = Configuration.DB_USERNAME
    val password = Configuration.DB_PASSWORD
    val url = Configuration.DB_URL
    val driver = Configuration.DB_DRIVER

    val spark_sql = spark.sqlContext
    val modelParamBean = JSON.parseObject(transactionBean.getModelParams, classOf[ModelParamBean])
    val rawRDD = srcRDD
    //新的数据格式
    val parsedRDD = rawRDD.map(line => line.split(",")).map {
      case Array(username, labelname) => ((username, labelname), 1)
    }.persist()


    val nameRDD = parsedRDD.mapPartitions(line => {
      val result = ListBuffer[(Int, String)]()
      while (line.hasNext) {
        val data = line.next()
        result.append((0, data._1._1))
      }
      result.iterator
    }).distinct().persist()


    val labelRDD = parsedRDD.mapPartitions(line => {
      val result = ListBuffer[(Int, String)]()
      while (line.hasNext) {
        val data = line.next()
        result.append((0, data._1._2))
      }
      result.iterator
    }).distinct().persist()

    val labelRDDBroadcast = sc.broadcast(labelRDD.collect())

    val allUserAndLabel = nameRDD.flatMap(userdata => {
      labelRDDBroadcast.value.flatMap(labeldata => {
        Some((userdata._2, labeldata._2), 0)
      })
    })

    nameRDD.unpersist()
    labelRDD.unpersist()

    val tmpRDD = parsedRDD.union(allUserAndLabel).reduceByKey((a, b) => a + b)


    val sortRDD = tmpRDD.mapPartitions(data => {
      val result = ListBuffer[UserLabel]()
      while (data.hasNext) {
        val userdata = data.next()
        val userlabel = UserLabel(userdata._1._1, userdata._1._2, userdata._2)
        result.append(userlabel)
      }
      result.iterator
    }).groupBy(userAndLabel => userAndLabel.username).sortBy(userAndLabels => userAndLabels._1)
      .map(labels => {
        (labels._1, labels._2.toList.sortBy(_.labelName))
      }).persist()


    tmpRDD.unpersist()


    val midmatrixRDD = sortRDD.mapPartitions(users => {
      val result = new ListBuffer[(String, String)]()
      while (users.hasNext) {
        val str: StringBuilder = new StringBuilder
        val user = users.next()
        user._2.foreach(xx => str.append(xx.isLabeled + " "))
        result.append((user._1, str.toString().trim))
      }
      result.iterator
    }).persist()
    sortRDD.unpersist()


    val parsedMatrixRDD = midmatrixRDD.mapPartitions(users => {
      val result = new ListBuffer[Vector]()
      while (users.hasNext) {
        val user = users.next()
        val userVector = Vectors.dense((user._2.split(" ").map(_.toDouble)))
        result.append(userVector)
      }
      result.iterator
    })

    val clusterModel = KMeans.train(parsedMatrixRDD, modelParamBean.numClusters, modelParamBean.numIterations, 10, KMeans.K_MEANS_PARALLEL, 100L)


    val clusterRDD = midmatrixRDD.map(user => {
      val userVector = Vectors.dense(user._2.split(" ").map(_.trim).map(_.toDouble))
      val index = clusterModel.predict(userVector)
      (user._1, index)
    })
    midmatrixRDD.unpersist()
    //根据分组结果提取特征
    val userAndLabelRDD = parsedRDD.mapPartitions { userlabeldata => {
      val result = new ListBuffer[(String, String)]()
      while (userlabeldata.hasNext) {
        val userlabel = userlabeldata.next()
        result.append((userlabel._1))
      }
      result.iterator
    }
    }
    parsedRDD.unpersist()

    val clusterRDDBroadcast = sc.broadcast(clusterRDD.collect())


    val resultRDD = userAndLabelRDD.flatMap(userlabel => {
      clusterRDDBroadcast.value.flatMap(clusterdata => {
        val result = new ListBuffer[(Int, (String, Int))]()
        if (clusterdata._1.equals(userlabel._1)) {
          result.append((clusterdata._2, (userlabel._2, 1)))
        }
        result.iterator
      })
    }).groupByKey().map(data => {
      val labels = data._2
      val stats = labels.groupBy(_._1).map(x => (x._1, x._2.size))
      (data._1, stats.toList.sortWith((x, y) => x._2 > y._2).take(3))
    }).map(tmp => {
      val features = tmp._2
      val str: StringBuilder = new StringBuilder
      features.foreach(xx => str.append(xx.toString() + " "))
      (tmp._1, str.toString().replace(",", ":").trim)
    })

    //转换成DataFrame保存到数据库中
    val transactionID = transactionBean.getId
    val dataSourceID = transactionBean.getDataSourceid
    import spark_sql.implicits._
    val resultDF = resultRDD.map(attributes => Result(Constants.KMEANS_LABEL_PREFIX + attributes._1, Constants.KMEANS_LABEL_MODEL, attributes._2, 0, transactionID, dataSourceID))
      .toDF()

    val connectionProperties = new Properties()
    connectionProperties.put(Constants.USER, username)
    connectionProperties.put(Constants.PASSWORD, password)


    resultDF.write.mode(SaveMode.Append).jdbc(url, Constants.DSMODELLABEL, connectionProperties)

    val dsmodellabelTable = spark_sql.read.format(Constants.JDBC).options(Map(
      Constants.URL -> url,
      Constants.DBTABLE -> Constants.DSMODELLABEL,
      Constants.DRIVER -> driver,
      Constants.USER -> username,
      Constants.PASSWORD -> password)).load()

    val labelid = dsmodellabelTable.select(dsmodellabelTable(Constants.ID), dsmodellabelTable(Constants.NAME)).where(Constants.TRANSACTIONID + Constants.EQ + Constants.SPACE + transactionID)


    val aRDD = labelid.rdd.map(line => (line(1).toString, line(0))).distinct()
    val bRDD = clusterRDD.map(line => (Constants.KMEANS_LABEL_PREFIX + line._2, line._1)).distinct()
    val cRDD = bRDD.join(aRDD).map(data => data._2)
    val cDF = cRDD.map(user => UserAndLabel(user._1, user._2.asInstanceOf[Integer].toInt, 1, CurrentTimeUtil.getNowDate(), transactionID, dataSourceID.toInt, 0)).toDF()

    cDF.write.mode(SaveMode.Append).jdbc(url, Constants.USERLABEL, connectionProperties)

  }


}
