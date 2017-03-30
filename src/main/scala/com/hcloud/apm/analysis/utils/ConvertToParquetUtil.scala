package com.hcloud.apm.analysis.utils

import java.net.URI

import com.alibaba.fastjson.JSON
import com.hcloud.apm.analysis.bean.FileSchemaBean
import com.hcloud.apm.analysis.common.SparkInstance
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.sql.types._
import org.slf4j.LoggerFactory

import scala.collection.mutable.ArrayBuffer


class ConvertToParquetUtil {
  private val logger = LoggerFactory.getLogger(classOf[ConvertToParquetUtil])

  /**
    * 将HDFS普通文件转换成Parquet文件
    *
    * @param sourceFilePath 普通文件存放在hdfs上的位置
    *                       //    * @param parquetFilePath 生成的Parquet文件存放位置
    * @param schemaJson     普通文件的字段定义
    * @return 是否转换成功
    */
   def covertFromHDFSToParquet(sourceFilePath: String, schemaJson: String): Boolean = {
    val schemaBean: FileSchemaBean = JSON.parseObject(schemaJson, classOf[FileSchemaBean])
    if (schemaBean == null) {
      return false
    }
    import scala.collection.JavaConversions._
    var schemaArray = ArrayBuffer[StructField]()
    for (fileField <- schemaBean.getFields) {
      logger.info(fileField.fieldName + ":" + fileField.fieldType + ":" + fileField.nullable)
      var field: StructField = null
      if (fileField.fieldType.equalsIgnoreCase("String")) {
        field = StructField(fileField.fieldName, StringType, fileField.nullable)
      } else if (fileField.fieldType.equalsIgnoreCase("Int")) {
        field = StructField(fileField.fieldName, IntegerType, fileField.nullable)
      } else if (fileField.fieldType.equalsIgnoreCase("Double")) {
        field = StructField(fileField.fieldName, DoubleType, fileField.nullable)
      }
      schemaArray += field
    }
    val schema_data = StructType(schemaArray)
    val sqlContext = SparkInstance.spark.sqlContext
    val df = sqlContext.read.format("com.databricks.spark.csv").schema(schema_data).option("delimiter", ",").load(sourceFilePath)
    val targetPath = sourceFilePath + ".parquet"
    val isExist = isExitsFile(targetPath)
    if (!isExist) {
      println("重新生成目标文件")
      df.distinct().coalesce(1).write.parquet(targetPath)
    }
    return true
  }

  /**
    * 判断指定文件是否存在
    *
    * @param filePath 文件路径
    * @return 存在返回true，不存在返回false
    */
  def isExitsFile(filePath: String): Boolean = {
    val conf: Configuration = new Configuration();
    conf.addResource(new Path("/etc/hadoop/conf/core-site.xml"))
    conf.addResource(new Path("/etc/hadoop/conf/hdfs-site.xml"))
    val hdfs: FileSystem = FileSystem.get(new URI("hdfs://cdh01:8020"), conf);
    val targetFilePath: Path = new Path(filePath);
    val isExists: Boolean = hdfs.exists(targetFilePath);
    isExists
    //      true
  }
}
