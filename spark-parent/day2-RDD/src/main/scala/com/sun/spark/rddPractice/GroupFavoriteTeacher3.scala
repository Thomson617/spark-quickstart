package com.sun.spark.rddPractice

import org.apache.spark.rdd.RDD
import org.apache.spark.{Partitioner, SparkConf, SparkContext}

import scala.collection.mutable

/**
  * Title   
  * Author  SunTao
  * Date  2018/10/23 11:47
  */
object GroupFavoriteTeacher3 {
  def main(args: Array[String]): Unit = {
    val sparkConf: SparkConf = new SparkConf().setAppName("GroupFavoriteTeacher3").setMaster("local")
    val sc = new SparkContext(sparkConf)
    sc.setLogLevel("WARN")
    val lines: RDD[String] = sc.textFile("D:\\workplace\\testFile\\teacher.log")

    val rdd1: RDD[Array[String]] = lines.map(x => x.split("/"))

    val pairsRDD: RDD[((String, String), Int)] = rdd1.map(x => {
      val subject: String = x(2).split("[.]")(0)
      val teacher = x(3)
      ((subject, teacher), 1)
    })
    //对结果进行聚合,将学科和老师联合做key
    val reduceRdd: RDD[((String, String), Int)] = pairsRDD.reduceByKey((a, b) => a + b)
    //val subjects = Array("cloud", "javaee", "php")
    val subjects = reduceRdd.map(ele => ele._1._1).distinct().collect()
    val partitioner = new SubjectPartitioner(subjects)
    val partitionerRdd: RDD[((String, String), Int)] = reduceRdd.partitionBy(partitioner)
    val result: RDD[((String, String), Int)] = partitionerRdd.mapPartitions(iter => {
      iter.toList.sortBy(ele => ele._2).reverse.take(3).iterator
    })

    result.collect().foreach(elem => println(elem))
    sc.stop()

  }
}

class SubjectPartitioner(subjects: Array[String]) extends Partitioner {

  val rules: mutable.HashMap[String, Int] = new mutable.HashMap[String, Int]()
  var index = 0
  for (elem <- subjects) {
    rules.put(elem, index)
    index += 1
  }

  override def numPartitions: Int = subjects.length

  override def getPartition(key: Any): Int = {
    val subject: String = key.asInstanceOf[(String, String)]._1
    rules(subject)
  }
}
