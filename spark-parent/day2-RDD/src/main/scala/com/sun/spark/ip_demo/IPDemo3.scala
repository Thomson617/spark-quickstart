package com.sun.spark.ip_demo

import org.apache.log4j.{Level, Logger}
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Title    查询日志中每个省所拥有的资源数
  * Author  SunTao
  * Date  2018/10/26 20:38
  */
object IPDemo3 {
  def main(args: Array[String]): Unit = {
    val rulesFilePath = "D:\\workplace\\testFile\\ip\\ip.txt"
    val accessFilePath = "D:\\workplace\\testFile\\ip\\20090121000132.394251.http.format"

    Logger.getLogger("org.apache.spark").setLevel(Level.OFF)
    val conf = new SparkConf().setAppName("IPDemo3").setMaster("local[4]")
    val sc = new SparkContext(conf)


    //1：读取IP规则资源库
    val ipRulesLines: RDD[String] = sc.textFile(rulesFilePath)
    // 2：整理IP规则
    // 117.93.244.0|117.93.255.255|1969091584|1969094655|亚洲|中国|江苏|盐城||电信|320900|China|CN|120.139998|33.377631
    val ipRules: RDD[(Long, Long, String)] = ipRulesLines.map(line => {
      val fields = line.split("[|]")
      val startNum = fields(2).toLong
      val endNum = fields(3).toLong
      val province = fields(6)
      (startNum, endNum, province)
    })
    //var result = ipRules.collect()
    // println(result.toBuffer)
    // 3: 将IP规则收集到Driver（collect）
    val allIpRulesInDriver: Array[(Long, Long, String)] = ipRules.collect()
    //4：将全部的ip资源库通过广播的方式发送到Executor
    // 广播之后，在Driver端获取了广播变量的引用（如果没有广播完，就不往下走）
    val broadcastRef: Broadcast[Array[(Long, Long, String)]] = sc.broadcast(allIpRulesInDriver)
    //5: 读取访问日志
    val accessLogLine: RDD[String] = sc.textFile(accessFilePath)

    //6: 整理访问日志
    val provinceAddOne: RDD[(String, Int)] = accessLogLine.map(line => {
      val fields = line.split("[|]")
      val ip = fields(1)
      val ipNum = MyUtils.ip2Long(ip)
      //通过广播变量的引用获取Executor中的全部IP规则，然后进行匹配ip规则
      val allIpRulesInExecutor: Array[(Long, Long, String)] = broadcastRef.value
      //根据规则进行查找，（用二分查找算法）
      var province = "未知"
      val index = MyUtils.binarySearch(allIpRulesInExecutor, ipNum)
      if (index != -1) {
        province = allIpRulesInExecutor(index)._3
      }
      (province, 1)
    })


    //7： 按照省份的访问次数进行计数
    val reduceRDD: RDD[(String, Int)] = provinceAddOne.reduceByKey(_ + _)
    //8：打印结果
    var result = reduceRDD.collect()
    println(result.toBuffer)


    // 计算结果，将计算好的结果写入到mysql中
    // 触发一个action，将数据写到mysql的逻辑函数传入
    // reduceRDD.foreach(t =>{
    // val conn = DriverManager.getConnection("jdbc:mysql://bigdata01:3306/bigdata", "root", "123456")
    // val pstm = conn.prepareStatement("Insert Into .... values(?.?)")
    // pstm.setString(1, t._1)
    // pstm.setInt(2, t._2)
    // pstm.executeUpdate()
    // pstm.close()
    // conn.close()
    // })
    //reduceRDD.foreachPartition(MyUtils.data2MySQL _)
    //9：释放资源
    sc.stop()
  }
}