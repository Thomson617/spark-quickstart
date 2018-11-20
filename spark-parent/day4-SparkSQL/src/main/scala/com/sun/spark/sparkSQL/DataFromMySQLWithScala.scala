package com.sun.spark.sparkSQL

import org.apache.spark.sql.SparkSession

/**
  * Title   使用Spark SQL从MySQL中加载
  * Author  SunTao
  * Date  2018/10/26 23:47
  */
object DataFromMySQLWithScala {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession
      .builder()
      .appName("DataFromMySQLWithScala")
      .master("local")
      .getOrCreate()

    spark.sparkContext.setLogLevel("WARN")

    /*val properties = new Properties()
    properties.put("user", "root")
    properties.put("password", "123456")
    val jdbcDF2 = spark.read
    .jdbc("jdbc:mysql://localhost/mydb", "student",properties)*/

    val jdbcDF = spark.read
      .format("jdbc")
      .option("url", "jdbc:mysql://localhost:3306/mydb")
      .option("dbtable", "student")
      .option("user", "root")
      .option("password", "123456")
      .load()

    jdbcDF.show()

    spark.stop()

  }
}
