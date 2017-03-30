package com.hcloud.apm.analysis.bean

import scala.beans.BeanProperty


class Field {
  @BeanProperty
  var fieldName:String = null
  @BeanProperty
  var fieldType:String = null
  @BeanProperty
  var nullable:Boolean = false
  @BeanProperty
  var isKey:Boolean = false


}
