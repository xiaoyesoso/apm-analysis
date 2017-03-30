/**
  * Created by Zorro on 10/24 024.
  */
package com.hcloud.apm.analysis.common

import java.net.{URLDecoder, URLEncoder}

import com.alibaba.fastjson.JSON
import com.github.ywilkof.sparkrestclient.{DriverState, SparkRestClient}
import com.hcloud.apm.analysis.bean.TransactionBean

import scala.collection.JavaConverters._

/**
  * 这是个类用于向Spark提交和终止任务
  */
object SparkClient {
  lazy val sparkRestClient = SparkRestClient.builder()
    .masterHost(Configuration.SPARK_MASTER_HOST)
    .sparkVersion("2.0.0")
    .build()

  /**
    * 向spark提交一个事务
    * @param transaction 事务实体
    * @return 提交的任务Id
    */
  def submitTransaction(transaction: TransactionBean): String = {
//    new String(List(JSON.toJSONString(transaction, false)).asJava.)
//    println("打印看看"+List(JSON.toJSONString(transaction, false)).asJava.get(0))

    val transactionJsonString =  URLEncoder.encode(JSON.toJSONString(transaction, false), "UTF-8");
//    encodeURI(encodeURI(a.value));
//    println("编码后的参数"+transactionJsonString)
//  println("解码后的参数"+URLDecoder.decode(transactionJsonString,"UTF-8"))

    sparkRestClient.prepareJobSubmit()
      .appName(transaction.getModelName)
      .appResource(Configuration.PROJECT_JAR_ADDRESS)
      .mainClass("com.hcloud.apm.analysis.common.SparkMain")
//      .appArgs(List(JSON.toJSONString(transaction, false)).asJava)
      .appArgs(List(JSON.toJSONString(transactionJsonString, false)).asJava)
       .submit()

  }

  /**
    * 查询任务执行状态
    * @param submissionId 提交任务Id
    * @return 返回任务当前的执行状态
    */
  def checkSubmissionStatus(submissionId: String): DriverState = {
    sparkRestClient.checkJobStatus().withSubmissionId(submissionId)
  }

  /**
    * 终止已提交的任务
    * @param submissionId 任务的Id
    * @return
    */
  def stopSubmission(submissionId: String): Boolean = {
    sparkRestClient.killJob().withSubmissionId(submissionId)
  }
}
