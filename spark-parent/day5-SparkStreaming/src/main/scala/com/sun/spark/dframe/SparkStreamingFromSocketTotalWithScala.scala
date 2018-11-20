package com.sun.spark.dframe

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Title   
  * Author  SunTao
  * Date  2018/10/28 17:33
  */
object SparkStreamingFromSocketTotalWithScala {

  def updateFuncion(newValues: Seq[Int], historyValues: Option[Int]): Option[Int] = {
    val newCount: Int = historyValues.getOrElse(0) + newValues.sum
    Option.apply(newCount)
  }

  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("SparkStreamingFromSocketTotalWithScala").setMaster("local[*]")
    val ssc: StreamingContext = new StreamingContext(conf, Seconds(5))
    ssc.sparkContext.setLogLevel("WARN")
    ssc.checkpoint("./ckscala")

    val lines: ReceiverInputDStream[String] = ssc.socketTextStream("spark-node01.sun.com", 9999)
    val pairs: DStream[(String, Int)] = lines.flatMap(_.split(" ")).map(x => (x, 1))
    val wordCounts = pairs.updateStateByKey(updateFuncion)
    wordCounts.print()

    //开启流式计算
    ssc.start()
    ssc.awaitTermination()

  }
}
