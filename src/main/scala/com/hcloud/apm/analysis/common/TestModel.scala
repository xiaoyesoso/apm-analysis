/**
  * Created by Zorro on 10/24 024.
  */
package com.hcloud.apm.analysis.common

import com.hcloud.apm.analysis.bean.TransactionBean
import org.apache.spark.sql.SaveMode

class TestModel extends AbstractModel {
  /**
    * 开始执行算法
    *
    * @param transaction 事务实体
    */
  override def fit(transaction: TransactionBean): Unit = {
    val dataRDD = sc.textFile(transaction.getDatasource.getFileLocation)
    val countRDD = dataRDD.flatMap(line=>line.split(" ")).map(word=>(word,1)).reduceByKey((x,y)=>x+y)
    val spark_ = spark
    import spark_.implicits._
    countRDD.toDF("word", "count").write.mode(SaveMode.Overwrite).parquet("hdfs://cdh01:8020/apm/test/result")
  }
}
