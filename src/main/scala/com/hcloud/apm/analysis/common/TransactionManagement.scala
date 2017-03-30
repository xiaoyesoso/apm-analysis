/**
  * Created by Zorro on 10/21 021.
  */
package com.hcloud.apm.analysis.common

import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.{Executors, TimeUnit}
import javax.annotation.{PostConstruct, Resource}

import com.github.ywilkof.sparkrestclient.DriverState
import com.hcloud.apm.analysis.bean.TransactionBean
import com.hcloud.apm.analysis.dao.TransactionMapper
import com.hcloud.apm.analysis.domain.{Transaction, TransactionExample}
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

import scala.collection.immutable.HashMap

/**
  * 这是一个事务管理的类，提供各种事务操作的方法
  * PS: "TransactionManager"不能用，Spring注入冲突...所以起了这么个奇怪的名字
  */
@Component
class TransactionManagement {

  private val logger = LoggerFactory.getLogger(classOf[TransactionManagement])

  @Resource
  private var transactionMapper: TransactionMapper =_

  /**
    * 正在运行的事务到提交任务的映射
    * (transactionId -> submissionId)
    */
  private var runningSubmissions = new HashMap[Int, String]()

  /**
    * 正在运行的事务到事务实体的映射
    */
  private var runningTransactions = new HashMap[Int, Transaction]()

  /**
    * 当前对象构造完成后执行的方法
    */
  @PostConstruct
  private def init(): Unit = {
    // 注册定时更新事务状态，每3秒执行一次
    Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable {
      override def run(): Unit = {
        try {
          runningTransactions.keys.foreach(recheckTransactionState)
        } catch {
          case e: Exception =>
        }
      }
    }, 0, 3, TimeUnit.SECONDS)

    // 程序启动时，应该从数据库中取出上次退出前正在执行的事务，放入正在运行的事务集合
    val example = new TransactionExample()
    example.createCriteria().andStatusEqualTo(DriverState.RUNNING.ordinal()+1)
    logger.error("czm"+example)
//    transactionMapper.selectByExample(example).foreach(addRunningTransaction())
  }

  /**
    * 添加到正在运行的事务
    * @param transaction  事务实体
    */
  private def addRunningTransaction(transaction: Transaction) = {
    val transactionId = transaction.getId.toInt
    logger.info("Transaction(id={}) is added to running transactions.", transactionId)
    runningSubmissions += (transactionId -> transaction.getSubmissionid)
    runningTransactions += (transactionId -> transaction)
  }

  /**
    * 从正在运行的事务中删除（事务执行完成或失败）
    * @param transactionId 事务Id
    */
  private def removeRunningTransaction(transactionId: Int) = {
    logger.info("Transaction(id={}) is finished or failed. removed from running transactions.", transactionId)
    runningSubmissions -= transactionId
    runningTransactions -= transactionId
  }

  /**
    * 重新检查事务的执行状态， 并将已完成或失败的事务移除正在执行的事务集合
    * @param transactionId 事务Id
    */
  private def recheckTransactionState(transactionId: Int) = {
    if (runningTransactions.contains(transactionId)) {
      // 映射表中还有这个事务

      val state = SparkClient.checkSubmissionStatus(runningSubmissions(transactionId))
      if(state.ordinal()==DriverState.FINISHED.ordinal()){
        runningTransactions(transactionId).setEndTime(getNowDate)
      }
      // 修改事务的状态，保存到数据库
      runningTransactions(transactionId).setStatus(state.ordinal() + 1)

      println(runningTransactions(transactionId).getStartTime)

      transactionMapper.updateByPrimaryKey(runningTransactions(transactionId))

      state match {
        case DriverState.SUBMITTED | DriverState.RUNNING | DriverState.RELAUNCHING =>
          // 任务还未执行完成
        case _ =>
          // 其他情况默认为任务执行完成或者失败了
          // 从正在执行的事务中删除
          removeRunningTransaction(transactionId)
      }
    }
  }

  /**
    * 开始一个事务
    * @param transaction 事务实体
    */
  def startTransaction(transaction: TransactionBean): Unit = {
    // 提交事务到Spark
    val submissionId = SparkClient.submitTransaction(transaction)
    transaction.setSubmissionid(submissionId)

    // 添加到已提交事务管理
    addRunningTransaction(transaction)
  }

  /**
    * 终止一个事务
    * @param transactionId 事务Id
    * @return 是否成功
    */
  def stopTransaction(transactionId: Int): Unit = {
    if (runningTransactions.contains(transactionId)) {
      val state = runningTransactions(transactionId).getStatus
      DriverState.values()(state - 1) match {
        case DriverState.SUBMITTED | DriverState.RUNNING | DriverState.RELAUNCHING =>
          // 任务还未执行完成
          SparkClient.stopSubmission(runningSubmissions(transactionId))
          recheckTransactionState(transactionId)
        case _ =>
          // 其他情况默认为任务执行完成或者失败了
          throw new RuntimeException("任务已经完成或失败！")
      }
    }
  }
  def getNowDate():String={
    val now:Date = new Date()
    val dateFormat: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val currentTime = dateFormat.format( now )
    currentTime
  }
}