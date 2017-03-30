package com.hcloud.apm.analysis.bean

import java.util
import java.util.ArrayList

import scala.beans.BeanProperty

/**
  * Created by 陈志民 on 2016/11/17.
  */
class Labels extends Serializable{
  @BeanProperty
  var labels:ArrayList[LabelBean] = new util.ArrayList[LabelBean]();

}
