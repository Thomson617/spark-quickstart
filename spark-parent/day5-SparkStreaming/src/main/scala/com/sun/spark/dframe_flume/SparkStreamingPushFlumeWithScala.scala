package com.sun.spark.dframe_flume

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.flume.{FlumeUtils, SparkFlumeEvent}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Title     Spark Streaming整合Flume: Push方式
  * Spark Streaming对接FlumeNG有两种方式:
  *     一种是FlumeNG将消息Push推给Spark Streaming;
  *     还有一种是Spark Streaming从Flume 中Poll拉取数据。
  *
  * Author  SunTao
  * Date  2018/10/28 20:50
  */
object SparkStreamingPushFlumeWithScala {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("SparkStreamingPushFlumeWithScala").setMaster("local[2]")
    val ssc: StreamingContext = new StreamingContext(conf, Seconds(10))
    ssc.sparkContext.setLogLevel("WARN")
    ssc.checkpoint("./flumescala")

    val pushStreaming: ReceiverInputDStream[SparkFlumeEvent] = FlumeUtils.createStream(ssc, "192.168.46.48", 8888)
    //获取flume中event的body{"headers":xxxxxxx,"body":xxxxx}
    val lines: DStream[String] = pushStreaming.map(x => new String(x.event.getBody.array()))
    val pairs: DStream[(String, Int)] = lines.flatMap(line => line.split(" ")).map(x => (x, 1))
    val wordCounts: DStream[(String, Int)] = pairs.updateStateByKey(updateFunction)
    wordCounts.print()

    ssc.start()
    ssc.awaitTermination()

  }

  def updateFunction(newValues: Seq[Int], historyValues: Option[Int]): Option[Int] = {
    val newCount: Int = historyValues.getOrElse(0) + newValues.sum
    Option.apply(newCount)
  }

}
