package com.hcloud.apm.analysis.personas.utils

/**
  * Created by 陈志民 on 2016/9/14.
  */

import org.apache.log4j.{Level, Logger}
import org.apache.spark.mllib.clustering.{KMeans, KMeansModel}
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.{SparkConf, SparkContext}
/**
  * Created by 陈志民 on 2016/9/6.
  *
  * 尝试确定KMeans中的K
  */
object KMeansKChoose {
  def main(args: Array[String]): Unit = {
    Logger.getRootLogger.setLevel(Level.OFF)
    val conf = new SparkConf().setAppName("KMeansKChoose").setMaster("local")
    val sc = new SparkContext(conf)
    val rawTrainingData = sc.textFile("file:///C:/Users/陈志民/Desktop/用户标签化/train2.csv")
    val parsedTrainingData = rawTrainingData.filter(!isColumnNameLine(_)).map(line => {
      Vectors.dense(line.split(",").map(_.trim).filter(!"".equals(_)).map(_.toDouble))
    }).cache()
    val ks:Array[Int] = Array(3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20)

    var kresult = ""


    ks.foreach(cluster => {
      val model:KMeansModel = KMeans.train(parsedTrainingData,cluster,30,1,"k-means||",100)

      val ssd = model.computeCost(parsedTrainingData)
      //println("sum of squared distances of points to their nearest center when k=" + cluster + " -> "+ ssd)

      kresult += "sum of squared distances of points to their nearest center when k=" + cluster + " -> "+ ssd+"\n"

    })



    sc.stop()
    println(kresult)
  }
  private def isColumnNameLine(line: String): Boolean = {
    if (line != null &&
      line.contains("Channel")) true
    else false
  }
}