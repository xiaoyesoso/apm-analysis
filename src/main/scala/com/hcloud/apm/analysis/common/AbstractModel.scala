/**
  * Created by Zorro on 10/21 021.
  */
package com.hcloud.apm.analysis.common

import com.hcloud.apm.analysis.bean.TransactionBean
import org.apache.spark.sql.SparkSession

/**
  * 一个抽象的模型（算法）
  */
abstract class AbstractModel {

  // SparkSession对象
  var spark: SparkSession = _

  // SparkContext对象
  lazy val sc = spark.sparkContext

  /**
    * 开始执行算法
    * @param transaction 事务实体
    */
  def fit(transaction: TransactionBean): Unit
}
