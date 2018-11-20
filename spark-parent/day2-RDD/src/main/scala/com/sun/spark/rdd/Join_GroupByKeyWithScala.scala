package com.sun.spark.rdd

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

/**
  * Title   
  * Author  SunTao
  * Date  2018/10/22 17:53
  */
object Join_GroupByKeyWithScala {
  def main(args: Array[String]): Unit = {
    //1:创建SparkConf对象
    val conf: SparkConf = new SparkConf()
      .setAppName("Join_GroupByKeyWithScala")
      .setMaster("local")
    //2:创建SparkContext对象
    val sc: SparkContext = new SparkContext(conf)
    sc.setLogLevel("WARN")
    //3:准备集合
    val list1: List[(String, Int)] = List(("tom", 1), ("jerry", 3), ("kitty", 2))
    val list2: List[(String, Int)] = List(("jerry", 2), ("tom", 1), ("shuke", 2))
    //4:并行化创建初始RDD
    val rdd1: RDD[(String, Int)] = sc.parallelize(list1)
    val rdd2: RDD[(String, Int)] = sc.parallelize(list2)
    //求join
    //val rdd3 = rdd1 join rdd2
    val rdd3 = rdd1.join(rdd2)
    println("rdd3--> "+rdd3.collect.toBuffer)
    //求并集
    //val rdd4 = rdd1.union(rdd2)
    val rdd4 = rdd1 union rdd2
    println("rdd4--> " + rdd4.collect.toBuffer)
    //按key进行分组
    val rdd5 = rdd4.groupByKey
    println("rdd5--> " + rdd5.collect.toBuffer)

    val rdd6: RDD[(String, Int)] = rdd4.reduceByKey((a, b) => a + b)
    println("rdd6--> " + rdd6.collect.toBuffer)
    //按value的降序排序(false为降序; true为升序,是默认值)
    val rdd7: RDD[(String, Int)] = rdd6.map(t => (t._2, t._1)).sortByKey(false).map(t => (t._2, t._1))
    println("rdd7--> " + rdd7.collect.toBuffer)

  }
}
