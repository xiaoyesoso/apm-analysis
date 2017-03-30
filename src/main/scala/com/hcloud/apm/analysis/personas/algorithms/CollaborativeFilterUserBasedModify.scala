package com.hcloud.apm.analysis.personas.algorithms

import org.apache.spark.{SparkConf, SparkContext}

object CollaborativeFilterUserBasedModify
{
  def main(args: Array[String]) {

    val sparkConf = new SparkConf().setAppName("cf user-based").setMaster("local")
    val sc = new SparkContext(sparkConf)


    val oriRatings = sc.textFile("file:///C:/Users/陈志民/Desktop/data2.txt").map(line => {
      val fields = line.split("\t")
      (fields(0).toLong, fields(1).toLong, fields(2).toInt)//这里需要修改
    })

    //将用户多自己评价过的商品按照评分进行排序
    val ratings = oriRatings.groupBy(k=>k._1).flatMap(x=>(x._2.toList.sortWith((x,y)=>x._3>y._3).take(100)))



    //将数据按照用户分组(1,CompactBuffer((1,0,5), (1,0,4), (1,3,4), (1,0,4), (1,4,3), (1,0,0), (1,0,0)))
    val user2manyItem = ratings.groupBy(tup=>tup._1)

    /**
      * (4,0,3,1)
      * (1,0,5,7)
      * (1,0,4,7)
      * (1,3,4,7)
      * (1,0,4,7)
      * (1,4,3,7)
      * (1,0,0,7)
      * (1,0,0,7)
      * (3,4,0,1)
      * (5,0,0,2)
      * (5,4,0,2)
      * (2,5,0,1)
      * */
    //获得每一个用户评价过的商品个数
    val numPrefPerUser = user2manyItem.map(grouped=>(grouped._1,grouped._2.size))
    //为每条记录增加对应用户商品数目
    val ratingsWithSize = user2manyItem.join(numPrefPerUser).
      flatMap(joined=>{
        joined._2._1.map(f=>(f._1,f._2,f._3,joined._2._2))
      })

    //根据商品id转换成(id,item)
    val ratings2 = ratingsWithSize.keyBy(tup=>tup._2)
     //得到这样的元组 (4,((1,4,3,7),(3,4,0,1))) 其中一定要保证第一个用户id小于第二个用户id
    val ratingPairs = ratings2.join(ratings2).filter(f=>f._2._1._1<f._2._2._1)

    //得到这样的矩阵(（用户1id,用户2id）,（两用户评分积,用户1评分，用户2评分，用户1评分的平方，用户2评分的平方，用户1商品数目，用户2商品数目）)
    val tempVectorCalcs = ratingPairs.map(data=>{
      val key = (data._2._1._1,data._2._2._1)
      val stats =
        (data._2._1._3*data._2._2._3,//rating 1 * rating 2
          data._2._1._3, //rating user 1
          data._2._2._3, //rating user 2
          math.pow(data._2._1._3, 2), //square of rating user 1
          math.pow(data._2._2._3,2), //square of rating user 2
          data._2._1._4,  //num prefs of user 1
          data._2._2._4) //num prefs of user 2
      (key,stats)
    })
    /**
      *
      * ((4,5),(1,0,3,0,9.0,0.0,1,2))
      * ((1,4),(5,39,13,15,57.0,45.0,7,1))
      * ((3,5),(1,0,0,0,0.0,0.0,1,2))
      * ((1,5),(6,0,16,0,66.0,0.0,7,2))
      * ((1,3),(1,0,3,0,9.0,0.0,7,1))
      * */
    val vectorCalcs = tempVectorCalcs.groupByKey().map(data=>{
      val key = data._1
      val vals = data._2
      val size = vals.size
      val dotProduct = vals.map(f=>f._1).sum
      val ratingSum = vals.map(f=>f._2).sum
      val rating2Sum = vals.map(f=>f._3).sum
      val ratingSeq = vals.map(f=>f._4).sum
      val rating2Seq = vals.map(f=>f._5).sum
      val numPref = vals.map(f=>f._6).max
      val numPref2 = vals.map(f=>f._7).max
      (key,(size,dotProduct,ratingSum,rating2Sum,ratingSeq,rating2Seq,numPref,numPref2))
    })
//    vectorCalcs.foreach(println(_))

    //
    /**
      * 形成对称矩阵
      * ((4,5),(1,0,3,0,9.0,0.0,1,2))
      * ((1,4),(5,39,13,15,57.0,45.0,7,1))
      * ((3,5),(1,0,0,0,0.0,0.0,1,2))
      * ((1,5),(6,0,16,0,66.0,0.0,7,2))
      * ((1,3),(1,0,3,0,9.0,0.0,7,1))
      * ((5,4),(1,0,0,3,0.0,9.0,2,1))
      * ((4,1),(5,39,15,13,45.0,57.0,1,7))
      * ((5,3),(1,0,0,0,0.0,0.0,2,1))
      * ((5,1),(6,0,0,16,0.0,66.0,2,7))
      * ((3,1),(1,0,0,3,0.0,9.0,1,7))
      *
      * */
    val inverseVectorCalcs = vectorCalcs.map(x=>((x._1._2,x._1._1),(x._2._1,x._2._2,x._2._4,x._2._3,x._2._6,x._2._5,x._2._8,x._2._7)))
    val vectorCalcsTotal = vectorCalcs ++ inverseVectorCalcs

    /**
      * (4,(5,NaN))
      * (1,(4,0.5281756358312352))
      * (3,(5,NaN))
      * (1,(5,NaN))
      * (1,(3,NaN))
      * (5,(4,NaN))
      * (4,(1,3.1291568780614356))
      * (5,(3,NaN))
      * (5,(1,NaN))
      * (3,(1,NaN))
      *
      * */
    //计算相似度
    val tempSimilarities =
    vectorCalcsTotal.map(fields => {
      val key = fields._1
      val (size, dotProduct, ratingSum, rating2Sum, ratingNormSq, rating2NormSq, numRaters, numRaters2) = fields._2
      val cosSim = cosineSimilarity(dotProduct, scala.math.sqrt(ratingNormSq), scala.math.sqrt(rating2NormSq))*
        size/(numRaters*math.log10(numRaters2+10))
       (key._1,(key._2, cosSim))
    })


    //
//
    /**
      * 获得每个用户相似度排名前五十的用户列表
      * (1,(4,0.5281756358312352))
      * (1,(5,NaN))
      * (1,(3,NaN))
      * (3,(5,NaN))
      * (3,(1,NaN))
      * (5,(4,NaN))
      * (5,(3,NaN))
      * (5,(1,NaN))
      * */
    val similarities = tempSimilarities.groupByKey().flatMap(x=>{
      x._2.map(temp=>(x._1,(temp._1,temp._2))).toList.sortWith((a,b)=>a._2._2>b._2._2).take(50)
    })

    similarities.foreach(println(_))
//    val temp = similarities.filter(x=>x._2._2.equals(Double.PositiveInfinity))
//
//    val similarTable = similarities.map(x=>(x._1,x._2._1,x._2._2))//.toDF()
//    //  hiveql.sql("use DatabaseName")
//    //similarTable.insertInto("similar_user_test",true)
//
//
//    // ratings format (user,(item,raing))
//    val ratingsInverse = ratings.map(rating=>(rating._1,(rating._2,rating._3)))
//
//    //statistics format ((user,item),(sim,sim*rating)),,,, ratingsInverse.join(similarities) fromating as (user,((item,rating),(user2,similar)))
//    val statistics = ratingsInverse.join(similarities).map(x=>((x._2._2._1,x._2._1._1),(x._2._2._2,x._2._1._2*x._2._2._2)))
//
//    // predictResult fromat ((user,item),predict)
//    val predictResult = statistics.reduceByKey((x,y)=>((x._1+y._1),(x._2+y._2))).map(x=>(x._1,x._2._2/x._2._1))
//
//
//    val filterItem = ratings.map(x=>((x._1,x._2),Double.NaN))
//    val totalScore = predictResult ++ filterItem
//
//    val finalResult = totalScore.reduceByKey(_+_).filter(x=> !(x._2 equals(Double.NaN))).
//      map(x=>(x._1._1,x._1._2,x._2)).groupBy(x=>x._1).flatMap(x=>(x._2.toList.sortWith((x,y)=>x._3>y._3).take(50)))
//
//    // println(finalResult)
//    finalResult.saveAsTextFile("file:///C:/Users/陈志民/Desktop/result")

    //val recommendTable = finalResult.toDF()
    //hiveql.sql("use DatabaseName")
    //recommendTable.insertInto("recommend_user_test",true)
  }

  // *************************
  // * SIMILARITY MEASURES
  // *************************

  /**
    * The correlation between two vectors A, B is
    *   cov(A, B) / (stdDev(A) * stdDev(B))
    *
    * This is equivalent to
    *   [n * dotProduct(A, B) - sum(A) * sum(B)] /
    *     sqrt{ [n * norm(A)^2 - sum(A)^2] [n * norm(B)^2 - sum(B)^2] }
    */
  def correlation(size : Double, dotProduct : Double, ratingSum : Double,
                  rating2Sum : Double, ratingNormSq : Double, rating2NormSq : Double) = {

    val numerator = size * dotProduct - ratingSum * rating2Sum
    val denominator = scala.math.sqrt(size * ratingNormSq - ratingSum * ratingSum) *
      scala.math.sqrt(size * rating2NormSq - rating2Sum * rating2Sum)+1

    numerator / denominator
  }

  /**
    * Regularize correlation by adding virtual pseudocounts over a prior:
    *   RegularizedCorrelation = w * ActualCorrelation + (1 - w) * PriorCorrelation
    * where w = # actualPairs / (# actualPairs + # virtualPairs).
    */
  def regularizedCorrelation(size : Double, dotProduct : Double, ratingSum : Double,
                             rating2Sum : Double, ratingNormSq : Double, rating2NormSq : Double,
                             virtualCount : Double, priorCorrelation : Double) = {

    val unregularizedCorrelation = correlation(size, dotProduct, ratingSum, rating2Sum, ratingNormSq, rating2NormSq)
    val w = size / (size + virtualCount)

    w * unregularizedCorrelation + (1 - w) * priorCorrelation
  }

  /**
    * The cosine similarity between two vectors A, B is
    *   dotProduct(A, B) / (norm(A) * norm(B))
    */
  def cosineSimilarity(dotProduct : Double, ratingNorm : Double, rating2Norm : Double) = {
    dotProduct / (ratingNorm * rating2Norm)
  }

  /**
    * The Jaccard Similarity between two sets A, B is
    *   |Intersection(A, B)| / |Union(A, B)|
    */
  def jaccardSimilarity(usersInCommon : Double, totalUsers1 : Double, totalUsers2 : Double) = {
    val union = totalUsers1 + totalUsers2 - usersInCommon
    usersInCommon / union
  }
}