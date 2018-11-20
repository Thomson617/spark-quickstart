package com.sun.spark.rddPractice

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

/**
  * Title   
  * Author  SunTao
  * Date  2018/10/23 11:47
  */
object GroupFavoriteTeacher1 {
  def main(args: Array[String]): Unit = {
    val sparkConf: SparkConf = new SparkConf().setAppName("GroupFavoriteTeacher1").setMaster("local")
    val sc = new SparkContext(sparkConf)
    sc.setLogLevel("WARN")
    val lines: RDD[String] = sc.textFile("D:\\workplace\\testFile\\teacher.log")

    val rdd1: RDD[Array[String]] = lines.map(x => x.split("/"))

    val pairsRDD: RDD[((String, String), Int)] = rdd1.map(x => {
      val subject: String = x(2).split("[.]")(0)
      val teacher = x(3)
      ((subject, teacher), 1)
    })

    /*
        val rdd1: RDD[Array[String]] = lines.map(x => x.split("//"))
        val rdd2: RDD[Array[String]] = rdd1.map(x => x(1)).map(x => x.split("/"))

        val pairsRDD: RDD[((String, String), Int)] = rdd1.map(x => x(1)).map(x => {
          val subject = x.split("\\.")(0)
          val teacher = x.split("/").map(y => y(1)).toString
          ((subject, teacher), 1)
        })
    */
    //对结果进行聚合,将学科和老师联合做key
    val reduceRdd: RDD[((String, String), Int)] = pairsRDD.reduceByKey((a, b) => a + b)
    //对结果进行分组
    val groupRdd: RDD[(String, Iterable[((String, String), Int)])] = reduceRdd.groupBy(ele => ele._1._1)

    val result1: Array[((String, String), Int)] = reduceRdd.sortBy(x => x._2, false).collect
    //调用scala的sortBy方法
    val result2: RDD[(String, List[((String, String), Int)])] = groupRdd.mapValues(iter => iter.toList.sortBy(x => x._2).reverse.take(3))


    result1.foreach(x => println(x))
    println("--------------------------------------")
    result2.foreach(x => println(x))

    sc.stop()

  }
}
