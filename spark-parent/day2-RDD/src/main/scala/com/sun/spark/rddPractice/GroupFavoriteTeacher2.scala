package com.sun.spark.rddPractice

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Title   
  * Author  SunTao
  * Date  2018/10/23 11:47
  */
object GroupFavoriteTeacher2 {
  def main(args: Array[String]): Unit = {
    val sparkConf: SparkConf = new SparkConf().setAppName("GroupFavoriteTeacher2").setMaster("local")
    val sc = new SparkContext(sparkConf)
    sc.setLogLevel("WARN")
    val lines: RDD[String] = sc.textFile("D:\\workplace\\testFile\\teacher.log")

    val rdd1: RDD[Array[String]] = lines.map(x => x.split("/"))

    val pairsRDD: RDD[((String, String), Int)] = rdd1.map(x => {
      val subject: String = x(2).split("[.]")(0)
      val teacher = x(3)
      ((subject, teacher), 1)
    })

    //对结果进行聚合,将学科和老师联合做key
    val reduceRdd: RDD[((String, String), Int)] = pairsRDD.reduceByKey((a, b) => a + b)

    //val subjects = Array("cloud", "javaee", "php")
    val subjects = reduceRdd.map(ele => ele._1._1).distinct().collect()
    for (elem <- subjects) {
      val result: Array[((String, String), Int)] = reduceRdd.filter(ele => ele._1._1 == elem).sortBy(x => x._2, false).take(3)
      result.foreach(x => println(x))
    }

    sc.stop()

  }
}
