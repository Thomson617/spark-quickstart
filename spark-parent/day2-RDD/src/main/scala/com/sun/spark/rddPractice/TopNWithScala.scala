package com.sun.spark.rddPractice

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

/**
  * Title   
  * Author  SunTao
  * Date  2018/10/23 10:12
  */
object TopNWithScala {
  def main(args: Array[String]): Unit = {
    val sparkConf: SparkConf = new SparkConf().setAppName("TopNWithScala ").setMaster("local")
    val sc = new SparkContext(sparkConf)
    sc.setLogLevel("WARN")
    val lines: RDD[String] = sc.textFile("D:\\workplace\\testFile\\access.log")

    val linesRDD: RDD[Array[String]] = lines.map(line => line.split(" "))
    val filterRdd: RDD[Array[String]] = linesRDD.filter(x => x.length > 10).filter(x => x(10) != "\"-\"")
    val value: RDD[(String, Int)] = filterRdd.map(x => x(10)).map(x => (x, 1)).reduceByKey((a, b) => a + b)
    //val sortRdd: RDD[(String, Int)] = value.map(x => (x._2, x._1)).sortByKey(false, 1).map(x => (x._2, x._1))
    val sortRdd: RDD[(String, Int)] = value.sortBy(x => x._2, false)
    val top5: Array[(String, Int)] = sortRdd.take(5)
    top5.foreach(result => println(result._1 + " --> " + result._2))

    sc.stop()
  }


}
