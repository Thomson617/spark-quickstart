package com.sun.spark.rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Title   
  * Author  SunTao
  * Date  2018/10/22 17:50
  */
object Union_Distinct_IntersectionWithScala {

  def main(args: Array[String]): Unit = {

    //1:创建SparkConf对象
    val conf: SparkConf = new SparkConf().setAppName("Union_Distinct_IntersectionWithScala").setMaster("local")
    //2:创建SparkContext对象
    val sc: SparkContext = new SparkContext(conf)
    sc.setLogLevel("WARN")
    //3:准备集合
    val list1: List[Int] = List(5, 6, 4, 3)
    val list2: List[Int] = List(1, 2, 3, 4)
    //4:并行化集合,获取初始RDD
    val rdd1: RDD[Int] = sc.parallelize(list1)
    val rdd2: RDD[Int] = sc.parallelize(list2)
    //求并集
    val rdd3 = rdd1.union(rdd2)
    //求交集
    val rdd4 = rdd1.intersection(rdd2)
    val c3 = rdd3.collect()
    //去重
    val c4 = rdd3.distinct.collect
    val c5 = rdd4.collect
    println(c3.toBuffer)
    println(c4.toBuffer)
    println(c5.toBuffer)
    //释放资源
    sc.stop()


  }

}
