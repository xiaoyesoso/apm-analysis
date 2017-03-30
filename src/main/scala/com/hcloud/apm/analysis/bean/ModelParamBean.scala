package com.hcloud.apm.analysis.bean

import scala.beans.BeanProperty


class ModelParamBean extends Serializable{

  @BeanProperty
  var numClusters:Int = _

  @BeanProperty
  var numIterations:Int = _

  @BeanProperty
  var features:Int = _

}
