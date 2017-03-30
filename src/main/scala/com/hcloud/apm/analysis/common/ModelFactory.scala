/**
  * Created by Zorro on 10/21 021.
  */
package com.hcloud.apm.analysis.common

import com.hcloud.apm.analysis.label.{BasicLabelModel, KMeansLabelModel}
import com.hcloud.apm.analysis.recommend.algorithms.ALSRecommendModel

/**
  * 模型工厂
  */
object ModelFactory {

  /**
    * 根据模型名称获取算法模型对象
    * @param model 模型名称
    * @return
    */
  def getModel(model: String): AbstractModel = {
    model match {
      case "ALSRecommend" =>
        new ALSRecommendModel()
      case "BasicLabelModel" =>
        new BasicLabelModel()
      case "KMeansLabelModel" =>
        new KMeansLabelModel()
      case _ =>
        new TestModel()
    }
  }

  /**
    * 获取一个实现了推荐特性的模型
    * @param model 模型名称
    * @return Recommender
    */
  def getRecommendModel(model: String): RecommendModel = {
    model match {
      case "ALSRecommend" =>
        new ALSRecommendModel()
      case _ =>
        null
    }
  }

  /**
    * 获得标签模型
    * @param model
    * @return
    */
  def getBasicLabelModel(model: String): BasicLabelModel = {
    model match {
      case "BasicLabelModel" =>
        new BasicLabelModel()
      case _ =>
        null
    }
  }
  /**
    * 获得标签模型
    * @param model
    * @return
    */
  def getKMeansLabelModel(model: String): KMeansLabelModel = {
    model match {
      case "KMeansLabelModel" =>
        new KMeansLabelModel()
      case _ =>
        null
    }
  }

}
