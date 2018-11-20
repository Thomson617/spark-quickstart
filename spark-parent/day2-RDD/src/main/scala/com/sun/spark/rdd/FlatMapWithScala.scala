package com.sun.spark.rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Title   
  * Author  SunTao
  * Date  2018/10/22 17:15
  */
object FlatMapWithScala {
  def main(args: Array[String]): Unit = {

    val conf: SparkConf = new SparkConf().setAppName("FlatMapWithScala").setMaster("local")
    val sc: SparkContext = new SparkContext(conf)
    val list: List[String] = List("hello word", "hello hadoop", "hello spark hadoop hive")
    val listRDD: RDD[String] = sc.parallelize(list)
    val words: RDD[String] = listRDD.flatMap(line => line.split(" "))

    val result: Array[String] = words.collect()

    //println(result.toBuffer)
    result.foreach(word => println(word))

    sc.stop()

  }
}
