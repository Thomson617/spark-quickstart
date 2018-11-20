package com.sun.spark.ip_demo

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable


/**
  * Title  作业:(spark-core综合练习)
  * 根据ip地址查询进行统计
  * ((经度,纬度),次数)
  * 经度-->longitude;    纬度-->latitude
  *
  * Author  SunTao
  * Date  2018/10/24 21:11
  */
object IPDemoWithBinary {
  def main(args: Array[String]): Unit = {

    val sparkConf: SparkConf = new SparkConf().setAppName("IPDemoWithBinary").setMaster("local[4]")
    val sc = new SparkContext(sparkConf)
    sc.setLogLevel("WARN")
    val lines1: RDD[String] = sc.textFile("D:\\workplace\\testFile\\ip\\20090121000132.394251.http.format")
    val lines2: RDD[String] = sc.textFile("D:\\workplace\\testFile\\ip\\ip.txt")

    //125.213.100.123
    val ips1Rdd: RDD[String] = lines1.map(x => x.split("\\|")).map(x => x(1))

    //1.0.1.0|1.0.3.255|16777472|16778239|亚洲|中国|福建|福州||电信|350100|China|CN|119.306239|26.075302
    val ips2Rdd: RDD[Array[String]] = lines2.map(x => x.split("\\|"))

    //ipArray={125,213,100,123}
    val ipTransfer: RDD[Long] = ips1Rdd.map(x => {
      val ipArray: Array[String] = x.split("[.]")
      var ipNum = 0L
      for (i <- ipArray) {
        ipNum = i.toLong | ipNum << 8L
      }
      ipNum
    })


    val ipTransfer2: Array[(Long, Int)] = ipTransfer.map(x => (x, 1)).reduceByKey(_ + _).collect

    //ips2Transfer=((16777472,16778239),((119.306239,26.075302,重庆),1))
    val rulesRDD: RDD[((Long, Long), ((String, String, String), Int))] = ips2Rdd.map(x => {
      val start = x(2).toLong
      val end = x(3).toLong
      val city = x(7)
      val longitude = x(13) //经度
      val latitude = x(14) //纬度
      ((start, end), ((longitude, latitude, city), 1))
    })

    var hashMap = new mutable.HashMap[(String, String, String), Int]()
    val rules: List[((Long, Long), ((String, String, String), Int))] = rulesRDD.toLocalIterator.toList

    //二分法查找
    for (elem <- ipTransfer2) {
      var low = 0
      var high = rules.length - 1
      while (low <= high) {
        val middle = (low + high) / 2
        if ((elem._1 >= rules(middle)._1._1) && (elem._1 <= rules(middle)._1._2))
          if (hashMap.contains(rules(middle)._2._1)) {
            hashMap(rules(middle)._2._1) += elem._2
          } else {
            hashMap.put(rules(middle)._2._1, elem._2)
          }
        if (elem._1 < rules(middle)._1._1)
          high = middle - 1
        else {
          low = middle + 1
        }
      }
    }

    val result: List[((String, String, String), Int)] = hashMap.toList.sortBy(_._2).reverse
    result.foreach(x => println(x))

    println("size --> " + ipTransfer.collect.length)

    // sc.parallelize(result).saveAsTextFile("C:\\Users\\孙涛\\Desktop\\aaa")
    sc.stop()
  }


}
