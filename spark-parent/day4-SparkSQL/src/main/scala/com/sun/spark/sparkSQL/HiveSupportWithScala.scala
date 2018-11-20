package com.sun.spark.sparkSQL

import org.apache.spark.sql.SparkSession

/**
  * Title   
  * Author  SunTao
  * Date  2018/10/26 23:47
  */
object HiveSupportWithScala {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("HiveSupportWithScala")
      .master("local[2]")
      .config("spark.sql.warehouse.dir", "D:\\workplace\\testFile\\spark-warehouse")
      .enableHiveSupport()
      .getOrCreate()

    spark.sparkContext.setLogLevel("WARN")

    //操作sql语句
    spark.sql("CREATE TABLE IF NOT EXISTS person (id int, name string, age int) row format delimited fields terminated by ' '")
    spark.sql("LOAD DATA LOCAL INPATH 'D:/workplace/testFile/people.txt' INTO TABLE person")
    //查询
    spark.sql("select * from person order by age desc").show()
    //释放资源
    spark.stop()

  }
}
