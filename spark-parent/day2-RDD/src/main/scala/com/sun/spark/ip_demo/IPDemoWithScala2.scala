package com.sun.spark.ip_demo

import java.sql.{Connection, DriverManager, PreparedStatement}

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

/**
  * Title  作业:(spark-core综合练习)
  * 根据ip地址查询进行统计
  * ((经度,纬度),次数)
  * 经度-->longitude;    纬度-->Latitude
  *
  * Author  SunTao
  * Date  2018/10/24 21:11
  */
object IPDemoWithScala2 {

  def main(args: Array[String]): Unit = {

    val sparkConf: SparkConf = new SparkConf().setAppName("IPDemoWithScala2").setMaster("local[4]")
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

    //aaa=((16777472,16778239),(119.306239,26.075302))
    val aaa: RDD[((Long, Long), ((String, String, String), Int))] = ips2Rdd.map(x => {
      val start = x(2).toLong
      val end = x(3).toLong
      val city = x(7)
      val longitude = x(13) //经度
      val latitude = x(14) //纬度
      ((start, end), ((longitude, latitude, city), 1))
    })

    //val list: List[Long] = ipTransfer.toLocalIterator.toList
    var hashMap = new mutable.HashMap[(String, String, String), Int]()
    val ipList: List[((Long, Long), ((String, String, String), Int))] = aaa.toLocalIterator.toList

    for (elem <- ipList) {
      for (one <- ipTransfer2) {
        if (one._1 >= elem._1._1 && one._1 <= elem._1._2) {
          if (hashMap.contains(elem._2._1)) {
            hashMap(elem._2._1) += one._2
          } else {
            hashMap.put(elem._2._1, one._2)
          }
        }
      }
    }

    val result: List[((String, String, String), Int)] = hashMap.toList.sortBy(_._2).reverse

    myFun(result)

    sc.stop()
  }



  def myFun(list: List[((String, String, String), Int)]): Unit = {
    var conn: Connection = null
    var ps: PreparedStatement = null
    val sql = "insert into iplocation(longitude, latitude,city,ips_count) values (?,?,?,?)"
    try {
      conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/spark", "root", "123456")
      list.foreach(data => {
        ps = conn.prepareStatement(sql)
        ps.setString(1, data._1._1)
        ps.setString(2, data._1._2)
        ps.setString(3, data._1._3)
        ps.setInt(4, data._2)
        ps.executeUpdate()
      })
      println("操作成功!")
    } catch {
      case e: Exception => println("操作失败!")
    } finally {
      if (ps != null) {
        ps.close()
      }
      if (conn != null) {
        conn.close()
      }
    }
  }


}