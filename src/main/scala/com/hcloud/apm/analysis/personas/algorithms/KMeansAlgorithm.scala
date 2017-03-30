package com.hcloud.apm.analysis.personas.algorithms

import com.hcloud.apm.analysis.personas.database.RD.SparkToJDBC
import org.apache.spark.mllib.clustering.KMeansModel
import org.apache.spark.mllib.linalg.{Vector, Vectors}
import org.apache.spark.rdd.RDD

import scala.collection.Map


object KMeansAlgorithm {

  /**
    * 检测当前行是否是excel的表头
    * @param line 每一行记录
    * @return true表示是表头，false代表不是表头
    */
  def isColumnNameLine(line: String): Boolean = {
    if (line != null &&
      line.contains("Channel")) true
    else false
  }


  /***
    * 统计每个聚类中包含的用户数目
    * @param clusters Kmeans聚类出的模型
    * @param dataRDD 用户数据RDD(salary,age,consumption, gender,...)不包含用户名
    * @return RDD(clusterid,用户数目)
    */
  def collectResult(clusters: KMeansModel, dataRDD: RDD[Vector]): RDD[(Int, Int)] = {

    //初始化聚类统计结果集合
    var countresult: Map[Int, Int] = Map[Int, Int]()

    var index: Int = 0
    clusters.clusterCenters.map(key => {
      countresult += (index -> 0)
      index += 1
    })
    dataRDD.sparkContext.broadcast(countresult)

    //统计每个聚类包含的数目
    val finalResult = dataRDD.map(testDataLine => {
      val predictedClusterIndex: Int = clusters.predict(testDataLine)
      (predictedClusterIndex, 1)
    }).reduceByKey((x, y) => x + y)

    finalResult
  }

  /**
    * 将每个聚类的特征转换成map集合
    * @param clusters Kmeans聚类出的模型
    * @param dataRDD RDD(收入，年龄，消费，性别)
    * @return map(clusterid->收入人数最多:平均年龄:平均消费)
    */
  def getLabelMap(clusters: KMeansModel, dataRDD: RDD[Vector]): Map[Int, String] = {

    val tempRDD = KMeansAlgorithm.getLabelRDD(clusters, dataRDD).cache()
    tempRDD.map(k => {
      (k._1, k._2)
    }).collectAsMap()

  }


  /**
    * 得到每一个聚类的特征
    * @param clusters Kmeans聚类出的模型
    * @param dataRDD RDD(收入，年龄，消费，性别)
    * @return RDD(clusterid,"收入人数最多:平均年龄:平均消费")
    *
    */
  def getLabelRDD(clusters: KMeansModel, dataRDD: RDD[Vector]): RDD[(Int, String)] = {
    //获取聚类中心
    var clusterresult: Map[Int, String] = Map[Int, String]()
    var clustercenter: Int = 0
    clusters.clusterCenters.foreach(x => {
      clusterresult += (clustercenter -> x.toString)
      clustercenter += 1
    })

    //初始化聚类统计结果集合
    var countresult: Map[Int, Int] = Map[Int, Int]()
    clusterresult.keys.foreach(key => {
      countresult += (key -> 0)
    })

    //统计每个聚类包含的数目
    val predicResult = dataRDD.map(testDataLine => {
      val predictedClusterIndex: Int = clusters.predict(testDataLine)
      (predictedClusterIndex, (testDataLine(0), testDataLine(1), testDataLine(2)))
    })

    val partitionResult = predicResult.groupByKey()
    //统计收入字段
    val finalResult = partitionResult.map(part => {
      val features = part._2.map(x => {
        var feature: String = ""
        val salary:Double = x._1
        if (salary < 8000) {
          feature = "收入低"
        } else if (salary < 12000) {
          feature = "收入偏低"
        } else if (salary < 20000) {
          feature = "收入中等"
        } else {
          feature = "收入高"
        }
        (feature, 1)
      })
      (part._1, features)
    }).map(data => {
      val temp = data._2.groupBy(k => k._1).map(k => {
        (k._1, k._2.size)
      })
      (data._1, temp)
    }).map(z => {
      val finaldata = z._2.toList.sortWith((x, y) => x._2 > y._2).take(1)
      (z._1, finaldata)
    }).map(k => {

      (k._1, k._2(0) + " 平均年龄:" + clusters.clusterCenters(k._1)(1).toInt + " 平均消费:" + clusters.clusterCenters(k._1)(2).toString)
    })

    finalResult
  }

  /**
    * 预测所有用户所属的类别
    * @param clusters Kmeans算法训练出的模型
    * @param dataRDD 用户数据(收入，年龄，消费，性别)
    * @return (用户数据，用户所属类别)
    */
   def predictAllUsersCluster(clusters: KMeansModel, dataRDD: RDD[(String,String)]): RDD[(String, Int)] = {
    val parsedTrainingData = dataRDD.map(line => {
      (line._1,Vectors.dense(line._2.split(",").map(_.trim).filter(!"".equals(_)).map(_.toDouble)))
    }).persist()

    val finalResult = parsedTrainingData.map(testDataLine => {
      val predictedClusterIndex:
      Int = clusters.predict(testDataLine._2)
      (testDataLine._1, predictedClusterIndex)
    })
    finalResult
  }
  /**
    * 统计每个聚类的聚类特征
    * @param clusters Kmeans算法训练出的模型
    * @param dataRDD 用户数据(收入，年龄，消费，性别)
    * @return (clusterid,"收入人数最多:平均年龄:平均消费",人数)
    */
  def getModelLabelStatics(clusters: KMeansModel, dataRDD: RDD[Vector]): RDD[(Int, (String, Int))] = {
    val labelRDD = getLabelRDD(clusters, dataRDD)
    val clusterRDD = KMeansAlgorithm.collectResult(clusters, dataRDD)
    val finalRDD = labelRDD.join(clusterRDD)
    finalRDD
  }


  /**
    * 将聚类的标签结果保存到数据库
    * @param clusters kmeans算法训练出的模型
    * @param dataRDD
    */
  def buildModelLabelStatics(clusters: KMeansModel, dataRDD: RDD[Vector]) = {
    KMeansAlgorithm.getModelLabelStatics(clusters, dataRDD).foreachPartition(SparkToJDBC.insertLabelData)
  }

  /**
    * 从excel中读取用户文件存入到数据库
    * @param dataRDD RDD(用户名，收入，年龄，消费，性别)
    */
  def buildUsers(dataRDD: RDD[String]) = {
    dataRDD.map(data => {
      val user = data.split(",")
      (user(0), user(1).toInt, user(2).toInt, user(3).toInt, user(4).toInt)
    }).foreachPartition(SparkToJDBC.insertAllUsers)
  }

  /**
    * 将用户标签保存到用户标签表
    * @param usercluseter RDD(用户名,用户类别)
    *
    */
  def buildUserAndLabel(usercluseter: RDD[(String, Int)]) = {
    val useridAndLabelidRDD = usercluseter.map(data => {
      val userid = SparkToJDBC.selectUserIDByName(data._1)
      val lanelid = SparkToJDBC.selectLabelidByLabelcenter(data._2)
      (userid.toInt, lanelid.toInt)
    }).cache()
    useridAndLabelidRDD.distinct().foreachPartition(SparkToJDBC.insertUserAndLabel)
  }
}
