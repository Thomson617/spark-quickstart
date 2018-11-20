package com.sun.spark.rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Title   
  * Author  SunTao
  * Date  2018/10/22 14:23
  */
object ParallelizeConnectionScala {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("ParallelizeConnectionScala").setMaster("local[2]")
    val sc = new SparkContext(sparkConf)
    val numbers = Array(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    val numberRDD: RDD[Int] = sc.parallelize(numbers)
    val sum: Int = numberRDD.reduce((v1, v2) => v1 + v2)
    println("1~10之间的累加和: " + sum)
    sc.stop()

  }

}
