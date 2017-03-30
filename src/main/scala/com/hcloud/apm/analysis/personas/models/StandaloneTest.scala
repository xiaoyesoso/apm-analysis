package com.hcloud.apm.analysis.personas.models

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by 陈志民 on 2016/10/9.
  */
object StandaloneTest {
  def main(args: Array[String]): Unit = {
//    val conf = new SparkConf().setAppName("StandAloneDemo")
//      .setMaster("spark://cdh01:7077")
//      .set("spark.cores.max","2")
//      .setJars(Seq("target/ApmAnalytics.jar"))
//        .set("spark.driver.host","10.82.60.240")
//    val sc:SparkContext = new SparkContext(conf)
//
//
//    val words = sc.textFile("hdfs://cdh01:8020/words")
//    val wordPairs = words.flatMap(_.split(" ")).map((_, 1)).cache
//    val result = wordPairs.reduceByKey(_ + _).map(_.swap).sortByKey().cache()
//
//
//    result.saveAsTextFile("hdfs://cdh01:8020/result003")
//
//    result.collect.toList
//    sc.stop()
  }
}
