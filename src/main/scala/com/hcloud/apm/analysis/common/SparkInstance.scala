/**
  * Created by Zorro on 10/24 024.
  */
package com.hcloud.apm.analysis.common

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

/**
  * 这是持有SparkSession和SparkContext的Spring组件
  * 使用Spring的@Autowired来引用
  */
//xiugai
object SparkInstance {
  // 提交到spark的app名称
  val appName = "ApmAnalytics"

  // 是否使用local模式运行(否则运行到spark集群)
  var local = true

  // 本机IP地址(远程调试时需要)
  val localDriverHost = "localhost"

  var sparkMasterHost: String = _
  var sparkMasterPort: String = _
  val sparkMaster = if (local) "local" else
    s"spark://${Configuration.SPARK_MASTER_HOST}:${Configuration.SPARK_MASTER_PORT}"

  lazy val spark: SparkSession = {
    val conf = new SparkConf().setAppName(appName)
      .setMaster(sparkMaster)
      .set("spark.cores.max", "4")
      .set("spark.executor.memory", "4G")
      .set("spark.sql.warehouse.dir", "file:///C:/tmp/spark-warehouse")
    conf.set("spark.driver.host", localDriverHost)
    SparkSession.builder().config(conf).getOrCreate()
  }

  lazy val sc = spark.sparkContext
}

