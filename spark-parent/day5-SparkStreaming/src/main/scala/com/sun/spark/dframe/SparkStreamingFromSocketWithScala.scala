package com.sun.spark.dframe

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Title   
  * Author  SunTao
  * Date  2018/10/28 17:33
  */
object SparkStreamingFromSocketWithScala {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("SparkStreamingFromSocketWithScala").setMaster("local[*]")
    val ssc: StreamingContext = new StreamingContext(conf, Seconds(5))
    ssc.sparkContext.setLogLevel("WARN")
    val lines: ReceiverInputDStream[String] = ssc.socketTextStream("spark-node01.sun.com", 9999)
    val wordCounts: DStream[(String, Int)] = lines.flatMap(_.split(" ")).map(x => (x, 1)).reduceByKey(_ + _)
    wordCounts.print()

    //开启流式计算
    ssc.start()
    ssc.awaitTermination()

  }
}
