package com.sun.spark.sparkSQL

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
  * Title   Spark SQL将数据写入到MySQ
  * Author  SunTao
  * Date  2018/10/26 23:48
  */
case class Student(id: Int, username: String, age: Int)

object Data2MysqlWithScala {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("Data2MysqlWithScala")
      .master("local")
      .getOrCreate()

    val sc: SparkContext = spark.sparkContext
    sc.setLogLevel("WARN")

    //3:读取本地文件,加载数据
    val lines: RDD[String] = sc.textFile("D:\\workplace\\testFile\\people.txt")
    val studentRDD: RDD[Student] = lines.map(x => x.split(" ")).map(x => Student(x(0).toInt, x(1), x(2).toInt))
    import spark.implicits._
    val studentDF: DataFrame = studentRDD.toDF()
    studentDF.createOrReplaceTempView("student")

    //注:此处导入的表不需要提前创建
    studentDF.write.format("jdbc")
      .option("url", "jdbc:mysql://localhost:3306/spark")
      .option("dbtable", "student")
      .option("user", "root")
      .option("password", "123456")
      .save()

    val resultDF: DataFrame = spark.sql("select * from student order by age desc")
    resultDF.show()
    spark.close()

  }
}
