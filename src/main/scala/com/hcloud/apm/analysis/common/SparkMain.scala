/**
  * Created by Zorro on 10/24 024.
  */
package com.hcloud.apm.analysis.common

import java.net.URLDecoder
import java.util.Properties

import com.alibaba.fastjson.JSON
import com.hcloud.apm.analysis.bean.TransactionBean
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession


/**
  * Spark Main函数，作为Driver执行算法模型(Model)
  */
object SparkMain {

  /**
    * 从application.properties中加载sparkMaster地址
    */
  lazy val sparkMaster = {
    val prop = new Properties()
    prop.load(getClass.getClassLoader.getResourceAsStream("application.properties"))
    s"spark://${prop.getProperty("spark.master.host")}:${prop.getProperty("spark.master.port")}"
  }

  def main(args: Array[String]): Unit = {
    println("进入解析")
    // 根据参数解析出模型和事务对象
    val Array(jsonTransaction) = args.take(1)
//    println("未解码前"+jsonTransaction)
    val parsedJson = URLDecoder.decode(jsonTransaction,"UTF-8")
    println("解码后:"+URLDecoder.decode(jsonTransaction,"UTF-8"))

    val transaction = JSON.parseObject(parsedJson.substring(1,parsedJson.length-1), classOf[TransactionBean])
    val model = ModelFactory.getModel(transaction.getModelName)

    // 初始化Spark
    val conf = new SparkConf().setAppName(transaction.getModelName)
      .setMaster(sparkMaster)
      .set("spark.cores.max", Configuration.SPARK_CORES_MAX)
      .set("spark.executor.memory", Configuration.SPARK_EXECUTOR_MEMORY)

    model.spark = SparkSession.builder().config(conf).getOrCreate()

    // 开始执行！
    model.fit(transaction)
    model.sc.stop()
  }
}
