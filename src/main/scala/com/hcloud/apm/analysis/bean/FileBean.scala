package com.hcloud.apm.analysis.bean

import java.util

import scala.beans.BeanProperty

/**
  * Created by 陈志民 on 2016/11/18.
  */
class FileSchemaBean {
  @BeanProperty
  var fields:util.ArrayList[Field] = null
  @BeanProperty
  var separator:String = null
  @BeanProperty
  var deleteFirstLine:Boolean = false

}
