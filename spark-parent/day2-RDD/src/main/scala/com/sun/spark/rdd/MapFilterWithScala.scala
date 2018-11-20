package com.sun.spark.rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Title   
  * Author  SunTao
  * Date  2018/10/22 17:15
  */
object MapFilterWithScala {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("MapFilterWithScala").setMaster("local")
    val sc: SparkContext = new SparkContext(conf)
    val numbers: List[Int] = List(5, 6, 4, 7, 3, 8, 2, 9, 1, 10)
    val numberRDD: RDD[Int] = sc.parallelize(numbers)
    val sortByRDD: RDD[Int] = numberRDD.map(_ * 2).sortBy(x => x, true)

    //val resultRDD: RDD[Int] = sortByRDD.filter(num => num < 12)
    val resultRDD: RDD[Int] = sortByRDD.filter(_ < 12)

    val array: Array[Int] = resultRDD.collect()
    println(array.toBuffer)
    sc.stop()

  }
}
