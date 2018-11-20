package com.sun.spark.rdd

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Title   
  * Author  SunTao
  * Date  2018/10/22 18:17
  */
object ReduceWithScala {
  def main(args: Array[String]): Unit = {
    //1:创建SparkConf对象
    val conf: SparkConf = new SparkConf()
      .setAppName("ReduceWithScala")
      .setMaster("local")
    //2:创建SparkContext对象
    val sc: SparkContext = new SparkContext(conf)
    sc.setLogLevel("WARN")
    val rdd1 = sc.parallelize(List(1, 2, 3, 4, 5)) //1+2=3    3+3=6    6+4=10  10 + 5= 15
    //reduce聚合
    val rdd2 = rdd1.reduce((a, b) => a + b)
    //rdd2.collect
    println(rdd2)
  }
}
