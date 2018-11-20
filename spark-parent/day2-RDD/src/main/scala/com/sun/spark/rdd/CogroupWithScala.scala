package com.sun.spark.rdd

import java.util.logging.{Level, Logger}

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Title   
  * Author  SunTao
  * Date  2018/10/22 18:13
  */
object CogroupWithScala {
  def main(args: Array[String]): Unit = {
    //1:创建SparkConf对象
    val conf: SparkConf = new SparkConf()
      .setAppName("CogroupWithScala")
      .setMaster("local")
    //2:创建SparkContext对象
    val sc: SparkContext = new SparkContext(conf)
    sc.setLogLevel("WARN")


    val list1 = List(("tom", 1), ("tom", 2), ("jerry", 3), ("kitty", 2))
    val list2 = List(("jerry", 2), ("tom", 1), ("jim", 2))
    //并行化集合
    val rdd1: RDD[(String, Int)] = sc.parallelize(list1)
    val rdd2 = sc.parallelize(List(("jerry", 2), ("tom", 1), ("jim", 2)))
    //cogroup
    val rdd3 = rdd1.cogroup(rdd2)
    //注意cogroup与groupByKey的区别
    println(rdd3.collect.toBuffer)
  }
}
