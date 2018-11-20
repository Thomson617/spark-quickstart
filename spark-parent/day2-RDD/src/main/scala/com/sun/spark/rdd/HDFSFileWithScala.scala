package com.sun.spark.rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Title   
  * Author  SunTao
  * Date  2018/10/22 15:52
  */
object HDFSFileWithScala {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setAppName("HDFSFileWithScala")
    val sc: SparkContext = new SparkContext(sparkConf)
    val textFile: RDD[String] = sc.textFile(args(0))
    val count = textFile.map(line=>line.length).reduce((a,b)=>a+b)
    println("文件中出现得单词数为:"+count)
    sc.stop()

  }
}
