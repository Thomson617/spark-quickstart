package com.sun.spark.sparkSQL

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession


/**
  * Title   
  * Author  SunTao
  * Date  2018/10/27 0:22
  */
case class Person(id: String, username: String, age: String)

object CreateDataFrameWithScala {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("CreateDataFrameWithScala")
      .master("local[2]")
      .getOrCreate()
    //手动导入隐式转换    import spark.implicits._
    import spark.implicits._
    val sc: SparkContext = spark.sparkContext
    sc.setLogLevel("WARN")

    //3:读取本地文件,加载数据
    val lines: RDD[String] = sc.textFile("D:\\workplace\\testFile\\people.txt")
    //4:将每一行数据进行切分,并转换成People对象
    val peopleRDD: RDD[Person] = lines.map(line => line.split(" ")).map(attr => Person(attr(0), attr(1), attr(2)))


    //5:使用toDF方法,将peopleRDD转换成DataFrame
    val peopleDF= peopleRDD.toDF()


    //==============================DSL语法====================================
    //1、显示DataFrame中的内容
    peopleDF.show()
    //2、显示DataFrame的schema信息
    peopleDF.printSchema()
    //3、显示DataFrame记录数
    println(peopleDF.count())
    //4、显示DataFrame的所有字段
    peopleDF.columns.foreach(println)
    //5、取出DataFrame的第一行记录
    println(peopleDF.head())
    //6、显示DataFrame中name字段的所有值
    peopleDF.select("username").show()
    //7、过滤出DataFrame中年龄大于25的记录
    peopleDF.filter($"age" > 25).show()
    //8、统计DataFrame中年龄大于25的人数
    println(peopleDF.filter($"age" > 25).count())
    //9、统计DataFrame中按照年龄进行分组，求每个组的人数
    peopleDF.groupBy("age").count().show()


    //==============================DSL语法====================================
    //==============================SQL语法====================================
    peopleDF.createOrReplaceTempView("tb_people")
    spark.sql("select * from tb_people").show()
    spark.sql("select * from tb_people where username='张三'").show()
    spark.sql("select * from tb_people order by age desc").show()


    //==============================SQL语法====================================
    //释放资源
    spark.stop()


  }
}


