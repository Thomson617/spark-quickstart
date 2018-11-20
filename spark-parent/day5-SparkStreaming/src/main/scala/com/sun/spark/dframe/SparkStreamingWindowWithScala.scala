package com.sun.spark.dframe

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}

/**
  * Title   
  * Author  SunTao
  * Date  2018/10/28 17:33
  */
object SparkStreamingWindowWithScala {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("SparkStreamingWindowWithScala").setMaster("local[*]")
    val ssc: StreamingContext = new StreamingContext(conf, Seconds(5))
    ssc.sparkContext.setLogLevel("WARN")
    ssc.checkpoint("./ckwindow")
    val lines: ReceiverInputDStream[String] = ssc.socketTextStream("spark-node01.sun.com", 9999)

    val pairs: DStream[(String, Int)] = lines.flatMap(_.split(" ")).map(x => (x, 1))
    //对pairs进行开窗统计
    //参数说明
    //reduceFunc:集合函数
    //windowDuration:窗口的宽度,如本例5秒切分一次RDD，框10秒，就会保留最近2次切分的RDD
    //slideDuration:表示window滑动的时间长度，即每隔多久执行本计算
    val result: DStream[(String, Int)] = pairs.reduceByKeyAndWindow((a:Int,b:Int)=>a+b,Seconds(10),Seconds(5))
    result.print()

    //开启流式计算
    ssc.start()
    ssc.awaitTermination()

  }
}
