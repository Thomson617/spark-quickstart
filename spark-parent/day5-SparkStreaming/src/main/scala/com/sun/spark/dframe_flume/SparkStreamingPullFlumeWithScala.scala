package com.sun.spark.dframe_flume

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.flume.{FlumeUtils, SparkFlumeEvent}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Title    Spark Streaming整合Flume: Poll方式
  * Author  SunTao
  * Date  2018/10/28 20:50
  */
object SparkStreamingPullFlumeWithScala {

  def updateFunction(newValues: Seq[Int], historyValues: Option[Int]): Option[Int] = {
    val newCount: Int = historyValues.getOrElse(0) + newValues.sum
    Option.apply(newCount)
  }

  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("SparkStreamingPullFlumeWithScala").setMaster("local[2]")
    val ssc: StreamingContext = new StreamingContext(conf, Seconds(10))
    ssc.sparkContext.setLogLevel("WARN")
    ssc.checkpoint("./flumescala")

    val pollingStream: ReceiverInputDStream[SparkFlumeEvent] = FlumeUtils.createPollingStream(ssc, "spark-node01.sun.com", 8888)
    //获取flume中event的body{"headers":xxxxxxx,"body":xxxxx}
    val lines: DStream[String] = pollingStream.map(x => new String(x.event.getBody.array()))
    val pairs: DStream[(String, Int)] = lines.flatMap(line=>line.split(" ")).map(x => (x, 1))
    val wordCounts: DStream[(String, Int)] = pairs.updateStateByKey(updateFunction)
    wordCounts.print()

    ssc.start()
    ssc.awaitTermination()

  }
}
