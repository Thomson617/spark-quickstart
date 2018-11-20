package com.sun.spark.iplocation

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
  * Title   
  * Author  SunTao
  * Date  2018/10/27 10:40
  */
object IPLocationWithSparkSQL {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("HiveSupportWithScala")
      .master("local")
      .enableHiveSupport()
      .getOrCreate()
    val sc: SparkContext = spark.sparkContext
    sc.setLogLevel("WARN")

    //3:读取本地文件,加载数据
    val lines: RDD[String] = sc.textFile("D:/workplace/testFile/ip/ip.txt")
    val lines2: RDD[String] = sc.textFile("D:/workplace/testFile/ip/20090121000132.394251.http.format")
    //4:将每一行数据进行切分,并转换成People对象
    val rulesRDD: RDD[IpLocation] = lines.map(line => line.split("[|]")).map(attr => IpLocation(attr(2).toLong, attr(3).toLong, attr(13), attr(14), attr(7)))

    val ipTransfer: RDD[Long] = lines2.map(line => line.split("[|]")).map(x => {
      val ipArray: Array[String] = x(1).split("[.]")
      var ipNum = 0L
      for (i <- ipArray) {
        ipNum = i.toLong | ipNum << 8L
      }
      ipNum
    })

    import spark.implicits._
    val rulesDF: DataFrame = rulesRDD.toDF()
    val ipTransferDF: DataFrame = ipTransfer.map(x => TempIp(x)).toDF()

    rulesDF.createOrReplaceTempView("iplocation")
    ipTransferDF.createOrReplaceTempView("tempIp")

    //查询
    //val sql: String = "select longitude, latitude,city,ip_transfer from tempIp left  join iplocation on ip_transfer >= start_ip and ip_transfer <= end_ip limit 30"
    val sql2: String = "select longitude, latitude,city, count(1) as number from tempIp left join iplocation on ip_transfer >= start_ip and ip_transfer <= end_ip group by longitude, latitude,city order by number desc"

    spark.sql(sql2).show()

    sc.stop()
  }

}

case class IpLocation(start_ip: Long, end_ip: Long, longitude: String, latitude: String, city: String)
case class TempIp(ip_transfer: Long)