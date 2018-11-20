package com.sun.spark.ip_demo

import java.sql
import java.sql.DriverManager

/**
  * Title   
  * Author  SunTao
  * Date  2018/10/26 20:44
  */
object MyUtils {
  //Ip转换成Long类型
  def ip2Long(ip: String): Long = {
    val fragments = ip.split("[.]")
    var ipNum = 0L
    for (i <- 0 until fragments.length) {
      ipNum = fragments(i).toLong | ipNum << 8L
    }
    ipNum
  }

  def binarySearch(lines: Array[(Long, Long, String)], ip: Long): Int = {
    var low = 0
    var high = lines.length - 1
    while (low <= high) {
      val middle = (low + high) / 2
      if ((ip >= lines(middle)._1) && (ip <= lines(middle)._2))
        return middle
      if (ip < lines(middle)._1)
        high = middle - 1
      else {
        low = middle + 1
      }
    }
    -1
  }

  def data2MySQL(it: Iterator[(String, Int)]) = {
    //一个迭代器代表一个分区，分区中有多条数据
    // 先获得一个JDBC连接
    val conn: sql.Connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/spark?characterEncoding=UTF-8", "root", "123456")
    //将数据通过Connection写入到数据库
    val pstm: sql.PreparedStatement = conn.prepareStatement("insert into access_log values(?,?)")
    //将分区中的数据一条一条写入到MySQL
    it.foreach(tp => {
      pstm.setString(1, tp._1)
      pstm.setInt(2, tp._2)
      pstm.executeUpdate()
    })
    //将分区中的数据全部写完之后，在关闭连接
    if (pstm != null) {
      pstm.close()
    }
    if (conn != null) {
      conn.close()
    }
  }
}


