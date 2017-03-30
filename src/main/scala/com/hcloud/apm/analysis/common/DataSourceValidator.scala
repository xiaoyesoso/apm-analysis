/**
  * Created by Zorro on 10/31 031.
  */
package com.hcloud.apm.analysis.common

import com.hcloud.apm.analysis.bean.DataSourceBean
import com.hcloud.apm.analysis.domain.Datasource
import org.springframework.stereotype.Component

/**
  * 对数据源进行格式校验
  */
@Component
class DataSourceValidator {

  /**
    * 校验数据源
    * @param ds 数据源实体
    * @return 错误信息列表
    */
  def validateDataSource(ds: Datasource): (Integer, java.util.List[String]) = {

    ds.getType.toInt match {
      case 0 => validateHDFSFile(ds)
    }
  }

  /**
    * 校验HDFS格式的数据源
    * @param ds 数据源实体
    * @return 错误信息总数，前10个错误信息的列表
    */
  private def validateHDFSFile(ds: Datasource): (Integer, java.util.List[String]) = {
    if (ds.getFieldSchema == null || ds.getFieldSchema.isEmpty) throw new Exception("empty schema!")
    val schema =
      try {
        val dataSourceBean = new DataSourceBean(ds)
        assert(dataSourceBean.getSchema.getFields != null)
        dataSourceBean.getSchema
      } catch {
        case _: Throwable => throw new Exception("error parsing schema, check format!")
      }

    if (ds.getFileLocation == null || ds.getFileLocation.isEmpty) throw new Exception("empty address!")

    val bFields = SparkInstance.sc.broadcast(schema.getFields)
    val accErrorList = SparkInstance.sc.collectionAccumulator[String]
    val accErrorCount = SparkInstance.sc.longAccumulator
    // 添加错误信息，在这里控制错误信息的数量
    val addError = (msg: String) => {
      accErrorCount.add(1)
      if (accErrorCount.value < 10) {
        accErrorList.add(msg)
      }
    }

    val data = SparkInstance.sc.textFile(ds.getFileLocation)
    var dataWithIndex = data.map(_.split(schema.getSeparator)).zipWithIndex()

    // 根据需要去掉数据首行
    if (schema.isDeleteFirstLine)
      dataWithIndex = dataWithIndex.filter(_._2 != 0)

    // 检查字段数是否一致
    dataWithIndex = dataWithIndex.filter { case (row, index) =>
      if (row.length != bFields.value.size) {
        addError(s"line $index: 字段数不一致！字段数${row.length}，期待为${bFields.value.size}")
        false
      } else true
    }

    // 数据类型和nullable检查
    dataWithIndex.foreach { case (row, index) =>
      for (i <- 0 until row.length) {
        val field = bFields.value.get(i)
        val name = field.getFieldName
        if (!field.getNullable && row(i).isEmpty)
          addError(s"line $index: 字段`$name`为空值，期待不为空值")
        else
          field.getFieldType.toLowerCase match {
            case "string" =>
            case "int" =>
              try row(i).toInt catch {
                case e: NumberFormatException =>
                  addError(s"line $index: 字段`$name`解析为Int失败，值为`${row(i)}`")
              }
            case "double" =>
              try row(i).toDouble catch {
                case e: NumberFormatException =>
                  addError(s"line $index: 字段`$name`解析为Double失败，值为`${row(i)}`")
              }
          }
      }
    }
    (accErrorCount.value.toInt, accErrorList.value)

  }
}
