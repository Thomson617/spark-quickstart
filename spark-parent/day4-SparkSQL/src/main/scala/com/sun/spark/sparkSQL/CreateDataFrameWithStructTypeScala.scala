package com.sun.spark.sparkSQL

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.apache.spark.sql.types.{StringType, StructField, StructType}

/**
  * Title   
  * Author  SunTao
  * Date  2018/10/26 23:46
  */
object CreateDataFrameWithStructTypeScala {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("CreateDataFrameWithStructTypeScala")
      .master("local[2]")
      .getOrCreate()

    val sc: SparkContext = spark.sparkContext
    sc.setLogLevel("WARN")

    //3:读取本地文件,加载数据
    val lines: RDD[String] = sc.textFile("D:\\workplace\\testFile\\people.txt")
    //4:将每一行数据进行切分,并转换成People对象
    val schemaStr = "id name age"
    val fields: Array[StructField] =schemaStr.split(" ")
      .map(fileName=>StructField(fileName,StringType,nullable=true))

    val schema: StructType = StructType(fields)
    val rowRDD: RDD[Row] =lines.map(_.split(" ")).map(attribute=>Row(attribute(0),attribute(1),attribute(2)))
    val frame: DataFrame = spark.createDataFrame(rowRDD,schema)
    frame.createOrReplaceTempView("people")
    spark.sql("select * from people").show()


    sc.stop()
  }
}
