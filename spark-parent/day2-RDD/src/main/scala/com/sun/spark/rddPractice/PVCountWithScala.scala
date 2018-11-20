package com.sun.spark.rddPractice

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Title   
  * Author  SunTao
  * Date  2018/10/23 9:57
  */
object PVCountWithScala {
  def main(args: Array[String]): Unit = {
    val sparkConf: SparkConf = new SparkConf().setAppName("PVCountWithScala ").setMaster("local")
    val sc = new SparkContext(sparkConf)
    sc.setLogLevel("WARN")
    val lines: RDD[String] = sc.textFile("D:\\workplace\\testFile\\access.log")
    val pvWithOne: RDD[(String, Int)] = lines.map(line => ("PV", 1))
    val lineCounts: RDD[(String, Int)] = pvWithOne.reduceByKey((a, b) => a + b)
    lineCounts.foreach(lineCount => println(lineCount._1 + "的访问量为:" + lineCount._2))
    sc.stop()
  }

}
