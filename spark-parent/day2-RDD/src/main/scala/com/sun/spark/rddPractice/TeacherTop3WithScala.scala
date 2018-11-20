package com.sun.spark.rddPractice

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

/**
  * Title   
  * Author  SunTao
  * Date  2018/10/23 11:47
  */
object TeacherTop3WithScala {
  def main(args: Array[String]): Unit = {
    val sparkConf: SparkConf = new SparkConf().setAppName("TeacherTop3WithScala ").setMaster("local")
    val sc = new SparkContext(sparkConf)
    sc.setLogLevel("WARN")
    val lines: RDD[String] = sc.textFile("D:\\workplace\\testFile\\teacher.log")

    /*
     val rdd1: RDD[Array[String]] = lines.map(x => x.split("/"))
     val rdd2: RDD[(String, Int)] = rdd1.map(x => x(3)).map(x => (x, 1))
    */
    val rdd1: RDD[Array[String]] = lines.map(x => x.split("//")).map(x => x(1)).map(x => x.split("/"))
    val rdd2: RDD[(String, Int)] = rdd1.map(x => x(1)).map(x => (x, 1))

    val result: RDD[(String, Int)] = rdd2.reduceByKey((a, b) => a + b)
    val top3: Array[(String, Int)] = result.sortBy(x => x._2, false).take(3)

    top3.foreach(result => println(result._1 + " --> " + result._2))


    sc.stop()

  }
}
