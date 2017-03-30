package com.hcloud.apm.analysis.bean

import java.util

import scala.beans.BeanProperty


class LabelBean extends Serializable {
  @BeanProperty
  var labelName: String = null
  @BeanProperty
  var fields: util.ArrayList[Column] = null
  @BeanProperty
  var sql: String = null

}

class ParsedColumns extends Serializable {
  @BeanProperty
  var sql: String = null
  @BeanProperty
  var columns: util.List[Column] = null

  def this(columns: util.List[Column]) {
    this()
    this.columns = columns
  }

  def parsedColumns() {
    val sb: StringBuffer = new StringBuffer(Constants.WHERE + Constants.SPACE)
    if (this.columns.size < 0) return
    import scala.collection.JavaConversions._
    for (column <- this.columns) {

      if (!column.getRelation.equals("null")) {
        sb.append(" ")
        sb.append(column.getRelation)
        sb.append(" ")
      }
      sb.append(column.getFieldName +Constants.SPACE+column.getOperation +Constants.SPACE+column.getParam)
    }
    sql = sb.toString
//    println(sb.toString)
  }
}
