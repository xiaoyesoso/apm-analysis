/**
  * Created by Zorro on 10/26 026.
  */
package com.hcloud.apm.analysis.common

import com.hcloud.apm.analysis.bean.{RecommendResult, TransactionBean}

/**
  * 推荐方法的特性
  */
trait Recommender {

  /**
    * 基于用户进行推荐
    * @param transactionBean 事务实体
    * @param userId           用户Id
    * @param resultNum        推荐结果
    * @return
    */
  def recommendByUser(transactionBean: TransactionBean, userId: String, resultNum: Int): List[RecommendResult]

  /**
    * 基于物品相似度进行推荐
    * @param transactionBean 事务实体
    * @param productId        物品Id
    * @param resultNum        推荐结果
    * @return
    */
  def recommendByProduct(transactionBean: TransactionBean, productId: String, resultNum: Int): List[RecommendResult]
}

/**
  * 可以推荐的Model
  */
abstract class RecommendModel extends AbstractModel with Recommender
