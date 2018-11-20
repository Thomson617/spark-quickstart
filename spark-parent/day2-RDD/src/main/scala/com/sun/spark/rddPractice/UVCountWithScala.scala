package com.sun.spark.rddPractice

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Title   
  * Author  SunTao
  * Date  2018/10/23 9:57
  */
object UVCountWithScala {
  def main(args: Array[String]): Unit = {
    val sparkConf: SparkConf = new SparkConf().setAppName("UVCountWithScala ").setMaster("local")
    val sc = new SparkContext(sparkConf)
    sc.setLogLevel("WARN")
    val lines: RDD[String] = sc.textFile("D:\\workplace\\testFile\\access.log")
    val ips: RDD[String] = lines.map(line => line.split(" ")).map(x => x(0))
    val uvWithOne: RDD[(String, Int)] = ips.distinct().map(x => ("UV", 1))
    val totalUV: RDD[(String, Int)] = uvWithOne.reduceByKey((a, b) => a + b)
    totalUV.foreach(x => println(x._1 + "独立访客量为:" + x._2))
    sc.stop()
  }

}
