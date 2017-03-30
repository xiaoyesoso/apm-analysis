/**
  * Created by Zorro on 10/21 021.
  */
package com.hcloud.apm.analysis.recommend.algorithms

import java.util

import com.alibaba.fastjson.{JSON, JSONObject}
import com.hcloud.apm.analysis.bean.{RecommendResult, TransactionBean}
import com.hcloud.apm.analysis.common.RecommendModel
import org.apache.spark.ml.recommendation.ALS.Rating
import org.apache.spark.ml.recommendation.{ALS, ALSModel}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.functions._
import org.jblas.DoubleMatrix

import scala.collection.mutable.WrappedArray

class ALSRecommendModel extends RecommendModel with Serializable {


  /**
    * 开始执行算法
    *
    * @param transaction 事务实体
    */
  override def fit(transaction: TransactionBean): Unit = {
    // 获取数据的HDFS地址
    val dataAddress = transaction.getDatasource.getFileLocation
    // 开始数据分析
    // ...
    val preparedData = prepare(transaction)

    val ratingsData = calcRatings(transaction, preparedData)
    // 算法实现，保存模型
    // ...
    val rawUserId = ratingsData.map(_._1).distinct().zipWithUniqueId()
    val rawProductId = ratingsData.map(_._2).distinct().zipWithUniqueId()
    val preuserId = rawUserId.collectAsMap()
    val preproductId = rawProductId.collectAsMap()
    //ALS模型需要一个由Rating记录构成的RDD，Rating类是对用户ID，商品ID和实际星级这些参数的封装
    val spark_ = new SQLContext(sc)
    import spark_.implicits._
    //ALS模型需要一个由Rating记录构成的RDD，Rating类是对用户ID，商品ID和实际星级这些参数的封装
    val preratings = ratingsData.map(data => (preuserId(data._1), preproductId(data._2), data._3))
    //case class Rating(userId: Int, productId: Int, rating: Float)
    val ratings = preratings.map(data => Rating(data._1.toInt, data._2.toInt, data._3.toFloat)).toDF().na.drop()
    //ratings.sample(false, 0.0001, seed = 0).show(10)
    val als = new ALS()
      .setMaxIter(10)
      .setRegParam(0.01)
      .setRank(50)
      .setUserCol("user")
      .setItemCol("item")
      .setRatingCol("rating")
    val model = als.fit(ratings)
    //val predictions = model.transform(ratings).na.drop()
    //predictions.show(10)

    model.save( s"hdfs://cdh01:8020/apm/${transaction.getId}/model")
    rawUserId.saveAsTextFile(s"hdfs://cdh01:8020/apm/${transaction.getId}/rawUserId")
    rawProductId.saveAsTextFile(s"hdfs://cdh01:8020/apm/${transaction.getId}/rawProductId")
    ratings.write.csv(s"hdfs://cdh01:8020/apm/${transaction.getId}/ratings")
  }

  def cosineSimilarity(vec1: DoubleMatrix, vec2: DoubleMatrix): Double = {
    vec1.dot(vec2) / (vec1.norm2() * vec2.norm2())
  }

  /**
    * 执行数据清理，去噪等操作，返回预处理过后的原始数据的RDD或DataFrame
    * @param transaction
    */
  def prepare(transaction: TransactionBean): RDD[String] = {
    val data = sc.textFile(transaction.getDatasource.getFileLocation)
    // 删除首行
    if (transaction.getDatasource.getSchema.isDeleteFirstLine) {
      data.zipWithIndex().filter(_._2 == 0).map(_._1)
    } else data
    return null
  }

  /**
    * 根据预处理后的数据计算评分，返回的结果是一个3列的RDD或DataFrame(用户id,商品id,评分)
    * @param transaction
    * @param preparedData 预处理后的数据，RDD或DataFrame，具体实现的时候把类型改一下
    */
  def calcRatings(transaction: TransactionBean, preparedData: RDD[String]): RDD[(String, String, Double)] = {
    //权重
    val weights: JSONObject = JSON.parseObject(transaction.getModelParams).getJSONObject("weights")
    // 得到最大权重
    val iterator: util.Iterator[AnyRef] = weights.values().iterator()
    var maxWeight: Double = 0.0
    while (iterator.hasNext) {
      val w: Double = iterator.next().toString.toDouble
      if(w > maxWeight) {
        maxWeight = w
      }
    }

    val fields = transaction.getDatasource.getSchema.getFields
    val separator = transaction.getDatasource.getSchema.getSeparator
    //从schema中提取所有行为
    val actions: Array[String] = new Array[String](fields.size() - 2)
    for (i <- 0 until fields.size() - 2) {
      actions(i) = fields.get(i+2).getFieldName
    }

    preparedData.map(line => {
      val words = line.split(separator)
      var rating: Double = 0.0
      var totalCount: Double = 0.0
      //计算行为总次数
      for (i <- 0 until fields.size() - 2) {
        totalCount += words(i + 2).toDouble
      }
      //计算每个行为的评分求和
      for (i <- 0 until fields.size() - 2) {
        rating += weights.getDouble(actions(i)) * (words(i + 2).toDouble / totalCount)
      }

      //(用户,商品,评分)
      (words(0), words(1), (rating * 10 / maxWeight * 100).toInt.toDouble)
    })
  }

  /**
    * 基于用户进行推荐
    * @param transactionBean 事务实体
    * @param userId           用户Id
    * @param resultNum        推荐结果
    * @return
    */
  def recommendByUser(transactionBean: TransactionBean, userId: String, resultNum: Int):List[RecommendResult] = {
    val spark_ = new SQLContext(sc)
    import spark_.implicits._
    val model = ALSModel.load(s"hdfs://cdh01:8020/apm/${transactionBean.getId}/model")
    val ratings = spark_.read.csv(s"hdfs://cdh01:8020/apm/${transactionBean.getId}/ratings").toDF("user","item","rating")
    val preuserData = sc.textFile(s"hdfs://cdh01:8020/apm/${transactionBean.getId}/rawUserId")
      .map { line => val Array(userName, trueuserId) = line.init.tail.split(",").map(_.trim).take(2)
        val rightUserId = trueuserId.toInt
        (userName, rightUserId)
      }
    val preproductData = sc.textFile(s"hdfs://cdh01:8020/apm/${transactionBean.getId}/rawProductId")
      .map {
        line => val Array(productName, trueproductId) = line.init.tail.split(",").map(_.trim).take(2)
          val rightproductId = trueproductId.toInt
          (productName, rightproductId)
      }
    val preuserId = preuserData.collectAsMap()
    val preproductId = preproductData.collectAsMap()
    val standarduserId = preuserId(userId)
    //用户推荐
    val k = resultNum
    val titles = preproductData.map(data => (data._2.toInt,data._1.toString)).toDF("item","title")
    val titles2 = preproductId.map(data => (data._2.toInt,data._1.toString))
        //titles.show(5)
        val product_notbuyed_productId = ratings.filter(ratings("user") === standarduserId).as('a).join(
          titles.as('b),$"a.item" === $"b.item","right").filter( $"a.item".isNull).select($"b.item", $"b.title")
        //product_notbuyed_productId.show(10,false)
        //println("Total number of products = " + titles.count())
        //println("Number of products rated by user = " + ratings.filter(ratings("user") === userId).count())
        //println("Number of products NOT rated by user = " + product_notbuyed_productId.count())
        val data_userId = product_notbuyed_productId.withColumn("user", lit(standarduserId))
        //data_userId.show(10, false)
        val predictions_userId = model.transform(data_userId).na.drop()
     val topk = predictions_userId.select("title", "prediction").sort($"prediction".desc)
        .rdd.map{rating =>
        val title = rating.get(0).asInstanceOf[String]
        val prediction = rating.get(1).asInstanceOf[Float].toDouble
       (title,prediction)
     }
      topk.map(rating => new RecommendResult(rating._1,rating._2/100.0)).take(k).toList
  }

  /**
    * 基于物品相似度进行推荐
    * @param transactionBean 事务实体
    * @param productId        物品Id
    * @param resultNum        推荐结果
    * @return
    */
  def recommendByProduct(transactionBean: TransactionBean, productId: String, resultNum: Int): List[RecommendResult] = {
    val spark_ = new SQLContext(sc)
    import spark_.implicits._
    val model = ALSModel.load(s"hdfs://cdh01:8020/apm/${transactionBean.getId}/model")
    val ratings = spark_.read.csv(s"hdfs://cdh01:8020/apm/${transactionBean.getId}/ratings").toDF("user","item","rating")
    val preproductData = sc.textFile(s"hdfs://cdh01:8020/apm/${transactionBean.getId}/rawProductId")
      .map {
        line => val Array(productName, trueproductId) = line.init.tail.split(",").map(_.trim).take(2)
          val rightproductId = trueproductId.toInt
          (productName, rightproductId)
      }

    val preproductId = preproductData.collectAsMap()
    val itemId = preproductId(productId)
    val titles = preproductId.map(array
    => (array._2, array._1))
    val itemFactor = model.itemFactors.select($"features").where($"id" === itemId).first()
      .getAs[WrappedArray[Float]](0).map(t => Float.float2double(t)).toArray
    //val itemFactor = model.itemFactors.selectExpr(s"select features where id = $itemId").show(10)
    val itemVector = new DoubleMatrix(itemFactor)
    val sims = model.itemFactors.rdd.map {case row=>
      val factorVector =
        new DoubleMatrix(row.get(1).asInstanceOf[WrappedArray[Float]].map(t => Float.float2double(t)).toArray)
      val sim = ((cosineSimilarity(factorVector, itemVector)).formatted("%.2f").toDouble)
      val id = row.get(0).toString.toInt
      (id, sim)
    }
    val k = resultNum
    val sortedSims = sims.top(k+1)(Ordering.by[(Int, Double), Double] { case
      (id, similarity) => similarity
    })
    //val sortedSims2 = sims.select("item","sim").sort($"sim".desc).show(10,false)
    sortedSims.slice(1,k+1).map{ case (id,sim) => new RecommendResult(titles(id),sim)}.toList

  }

}
