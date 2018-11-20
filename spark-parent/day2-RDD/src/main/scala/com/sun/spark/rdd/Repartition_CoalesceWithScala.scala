package com.sun.spark.rdd

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Title   
  * Author  SunTao
  * Date  2018/10/22 19:38
  */
object Repartition_CoalesceWithScala {
  def main(args: Array[String]): Unit = {
    //1:创建SparkConf对象
    val conf: SparkConf = new SparkConf().setAppName("Repartition_CoalesceWithScala").setMaster("local")
    //2:创建SparkContext对象
    val sc: SparkContext = new SparkContext(conf)
    sc.setLogLevel("WARN")
    //3:准备集合
    val rdd = sc.parallelize(1 to 10, 3)
    rdd.persist()
    //利用repartition改变rdd分区数
    //减少分区
    println(rdd.repartition(2).partitions.size)
    //增加分区
    println(rdd.repartition(4).partitions.size)
    //利用coalesce改变rdd分区数
    //减少分区
    println(rdd.coalesce(2).partitions.size)
  }

}
