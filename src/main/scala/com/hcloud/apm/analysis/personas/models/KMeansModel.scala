package com.hcloud.apm.analysis.personas.models

import com.hcloud.apm.analysis.personas.algorithms.KMeansAlgorithm
import org.apache.spark.mllib.clustering.{KMeans, KMeansModel}
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.{SparkConf, SparkContext}

/**
  *
  *
  * 参数:file:///C:/Users/陈志民/Desktop/用户标签化/test.csv 9 30 1
  *
  */
object KMeansModel {


  def main(args: Array[String]): Unit = {

    if (args.length < 4) {
      println("Usage:KMeansClustering trainingDataFilePath numClusters numIterations runTimes")
      sys.exit(1)
    }

    val conf = new SparkConf().setAppName("KMeansLabel").setMaster("local")
//    val conf = new SparkConf().setAppName("KMeansLabel").setMaster("spark://10.82.81.45:7077").set("spark.cores.max","2")
//    val conf = new SparkConf().setAppName("KMeansLabel")
    val sc = new SparkContext(conf)

    //包含用户名的完整数据
    val rawData = sc.textFile(args(0))

//    val rawDataAndIDRDD =rawData.distinct().zipWithUniqueId()
//    val nameAndIdRDD = rawDataAndIDRDD.map(line=>(line._1.substring(0,line._1.indexOf(",")),line._2))

    val rawTrainingData =rawData.map(line=>line.substring(line.indexOf(',')+1))

    val parsedTrainingData = rawTrainingData.filter(!KMeansAlgorithm.isColumnNameLine(_)).map(line => {
      Vectors.dense(line.split(",").map(_.trim).filter(!"".equals(_)).map(_.toDouble))
    }).persist()

    val numClusters = args(1).toInt
    val numIterations = args(2).toInt
    val runTimes = args(3).toInt
    var clusterIndex: Int = 0
    val clusters: KMeansModel = KMeans.train(parsedTrainingData, numClusters, numIterations, runTimes,"k-means||",100)

    //填充user表
//    KMeansAlgorithm.buildUsers(rawData)

//    填充label表
     KMeansAlgorithm.buildModelLabelStatics(clusters,parsedTrainingData)

    //填充user_label表
    val parsedRawRDD = rawData.map(data=>(data.substring(0,data.indexOf(",")),data.substring(data.indexOf(",")+1)))
    val usercluseter = KMeansAlgorithm.predictAllUsersCluster(clusters,parsedRawRDD)
    KMeansAlgorithm.buildUserAndLabel(usercluseter)
     parsedTrainingData.unpersist()
    sc.stop()

  }


}
