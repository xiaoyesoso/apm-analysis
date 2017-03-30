package com.hcloud.apm.analysis.label

import java.util.Properties
import com.alibaba.fastjson.JSON
import com.hcloud.apm.analysis.bean.{Constants, TransactionBean, _}
import com.hcloud.apm.analysis.common.{AbstractModel, Configuration}
import com.hcloud.apm.analysis.utils.CurrentTimeUtil
import org.apache.spark.sql.{DataFrame, Dataset}
import com.hcloud.apm.analysis.utils.ConvertToParquetUtil
import org.apache.spark.sql.SaveMode
import org.slf4j.LoggerFactory
import scala.collection.JavaConversions._

case class UserAndLabel(userid: String, labelid: Int, labelType: Int, time: String, transactionid: Int, dataSourceid: Int, overrided: Int) extends Serializable

case class UserAndLabelName(username: String, labelname: String)

class BasicLabelModel extends AbstractModel with Serializable {
  private val logger = LoggerFactory.getLogger(classOf[BasicLabelModel])

  /**
    * 开始执行算法
    * @param transaction 事务实体
    */
  override def fit(transaction: TransactionBean): Unit = {
    val parquetFileDF = loadDataSource(transaction)
    exeLabeling(transaction, parquetFileDF)
  }

  /**
    * 加载数据源，转换成parquet文件
    * @param transactionBean
    * @return 转换成parquet文件的DataFrame
    */
  def loadDataSource(transactionBean: TransactionBean): DataFrame = {
    logger.info("loadDataSource/start")
    val spark_sql = spark.sqlContext
    val filepath = Configuration.HDFS_DATA_PREFIX + transactionBean.getDatasource.getFileLocation + "/0/" + transactionBean.getDataSourceid + ".csv"
    val parquetUtil = new ConvertToParquetUtil
    val schemaJson = transactionBean.getDatasource.getFieldSchema
    parquetUtil.covertFromHDFSToParquet(filepath, schemaJson)
    val parquetFileDF = spark_sql.read.parquet(filepath + Constants.SUFFIX)
    logger.info("loadDataSource/end")
    parquetFileDF
  }

  /**
    * 打标签的逻辑控制层
    * @param transactionBean
    * @param parquetFileDF
    */
  def exeLabeling(transactionBean: TransactionBean, parquetFileDF: DataFrame): Unit = {
    logger.info("exeLabeling/start")

    val spark_sql = spark.sqlContext

    val selectName: String = getSelectName(transactionBean.getDatasource.getSchema)

    val labels: Labels = getLabelsSQL(transactionBean.getLabelSnapshot)

    parquetFileDF.createOrReplaceTempView(Constants.PARQUETFILE)

    val srcData = loadDB()
    srcData.persist()

    val datasourceID = transactionBean.getDataSourceid
    val transactionID = transactionBean.getId
    val currentTime = CurrentTimeUtil.getNowDate()
    val filepath = Configuration.HDFS_DATA_PREFIX + transactionBean.getDatasource.getFileLocation
    var nameAndLabelDFS: Dataset[UserAndLabel] = null

    for (label <- labels.getLabels) {
      val labelSQL = new StringBuilder
      labelSQL.append(Constants.SELECT + Constants.SPACE + selectName + Constants.SPACE + Constants.FROM + Constants.SPACE + Constants.PARQUETFILE + Constants.SPACE + label.getSql)
      import spark_sql.implicits._
      val namesDF = spark.sql(labelSQL.toString())
      namesDF.persist()
      if (namesDF.count() > 0) {
        val labelName = label.labelName
        val labelID: Int = getLabelIDByLabelName(srcData, labelName, datasourceID)
        if (labelID != -1) {
          val nameAndLabelDF = namesDF.map(user => {
            UserAndLabel(user(0).toString, labelID, 0, currentTime, transactionID, datasourceID.toInt, 0)
          })
          if (nameAndLabelDFS == null) {
            nameAndLabelDFS = nameAndLabelDF
          } else {
            nameAndLabelDFS = nameAndLabelDFS.union(nameAndLabelDF)
          }
        }
        val midFileDF = namesDF.rdd.map(data => UserAndLabelName(data.getString(0).toString, labelName)).toDF()
        midFileDF.write.mode(SaveMode.Append).csv(filepath + "/1/" + datasourceID + ".csv")
      }
      namesDF.unpersist()
    }
    srcData.unpersist()

    val connectionProperties = new Properties()
    connectionProperties.put(Constants.USER, Configuration.DB_USERNAME)
    connectionProperties.put(Constants.PASSWORD, Configuration.DB_PASSWORD)
    nameAndLabelDFS.write.mode(SaveMode.Append).jdbc(Configuration.DB_URL, Constants.USERLABEL, connectionProperties)

    logger.info("exeLabeling end")

  }

  /**
    * 加载数据库中的dsbasiclabel
    * @return
    */
  def loadDB(): DataFrame = {
    val spark_sql = spark.sqlContext

    spark_sql.read.format(Constants.JDBC).options(Map(
      Constants.URL -> Configuration.DB_URL,
      Constants.DBTABLE -> Constants.DSBASICLABEL,
      Constants.DRIVER -> Configuration.DB_DRIVER,
      Constants.USER -> Configuration.DB_USERNAME,
      Constants.PASSWORD -> Configuration.DB_PASSWORD)).load()
  }

  /**
    * 将定义的标签条件转换成部分SQL语句
    * @param labelSnapshot
    * @return
    */
  def getLabelsSQL(labelSnapshot: String): Labels = {
    val labels: Labels = JSON.parseObject(labelSnapshot, classOf[Labels])
    for (label <- labels.getLabels) {
      val parsedColumns: ParsedColumns = new ParsedColumns(label.fields)
      parsedColumns.parsedColumns()
      label.setSql(parsedColumns.getSql)
    }
    labels
  }

  /**
    * 根据标签名数据源id获得MySQL数据库中的标签id
    * @param srcData
    * @param labelName
    * @param datasourceID
    * @return
    */
  def getLabelIDByLabelName(srcData: DataFrame, labelName: String, datasourceID: Int): Int = {
    val labelIDDF = srcData.select(Constants.ID).where("name = '" + labelName + "'" + " and " + " dataSourceid = " + datasourceID)
    var labelID: Int = -1
    if (labelIDDF.count() > 0) {
      labelID = labelIDDF.first().getInt(0)
    }
    labelID
  }

  /**
    * 得到上传数据源中的主键字段
    * @param dataSourceSchema
    * @return
    */
  def getSelectName(dataSourceSchema: DataSourceSchema): String = {
    val selectName: StringBuilder = new StringBuilder
    val schema = dataSourceSchema
    if (schema != null) {
      for (field <- schema.getFields) {
        if (field.getIsKey) {
          selectName.append(field.getFieldName)
        }
      }
    }
    selectName.toString()
  }

  /**
    * 获得完整的SQL查询语句
    * @param selectName
    * @param sql
    * @return
    */
  def getCompleteSQL(selectName:String,sql:String): String ={
    val labelSQL = new StringBuilder
    labelSQL.append(Constants.SELECT + Constants.SPACE + selectName +
      Constants.SPACE + Constants.FROM + Constants.SPACE + Constants.PARQUETFILE
      + Constants.SPACE + sql)
    labelSQL.toString()
  }


}
