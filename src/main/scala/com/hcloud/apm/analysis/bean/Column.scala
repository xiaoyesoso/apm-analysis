package com.hcloud.apm.analysis.bean

import scala.beans.BeanProperty

class Column extends Serializable{
  @BeanProperty
   var fieldName: String = null
  @BeanProperty
   var operation: String = null
  @BeanProperty
  var param: String = null
  @BeanProperty
  var relation: String = null




}
