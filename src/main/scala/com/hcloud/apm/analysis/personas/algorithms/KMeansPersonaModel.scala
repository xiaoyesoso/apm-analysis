package com.hcloud.apm.analysis.personas.algorithms

import com.alibaba.fastjson.JSON
import com.hcloud.apm.analysis.bean.TransactionBean
import com.hcloud.apm.analysis.common.AbstractModel

import scala.collection.mutable.ListBuffer
case class Field(name:String,zhName:String,fieldType:String,nullable:String)
class KMeansPersonasModel extends AbstractModel{
  /**
    * 计算相似度
    *
    * @param transaction 事务实体
    */
  def fit(transaction: TransactionBean): Unit = {

    val dataschema = JSON.parseObject(transaction.getDatasource.getFieldSchema).getJSONArray("schema")

    var schemaList:ListBuffer[Field] =  new ListBuffer[Field]
    //解析整个字段
    for(i<-0 until(dataschema.size())){
      val fieldName = dataschema.getJSONObject(i).getString("name")
      val fieldzhName = dataschema.getJSONObject(i).getString("zhName")
      val fieldType = dataschema.getJSONObject(i).getString("type")
      val fieldnullable = dataschema.getJSONObject(i).getString("nullable")
      val field = Field(fieldName,fieldzhName,fieldType,fieldnullable)
      schemaList+=(field)
    }

    println(schemaList(2).zhName)

  }
  /**
    * 执行数据清理，去噪等操作，返回预处理过后的原始数据的RDD或DataFrame
    * @param transaction
    */
  def prepare(transaction: TransactionBean) = {

  }

  /**
    * 根据预处理后的数据计算评分，返回的结果是一个3列的RDD或DataFrame(用户id,商品id,评分)
    * @param transaction
    * @param preparedData 预处理后的数据，RDD或DataFrame，具体实现的时候把类型改一下
    */
  def calcRatings(transaction: TransactionBean, preparedData: Any) = {

  }

}
