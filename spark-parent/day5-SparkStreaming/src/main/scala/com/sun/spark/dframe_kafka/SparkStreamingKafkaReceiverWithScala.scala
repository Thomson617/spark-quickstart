package com.sun.spark.dframe_kafka

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Title   
  * Author  SunTao
  * Date  2018/10/29 23:45
  */
object SparkStreamingKafkaReceiverWithScala {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("SparkStreamingKafkaReceiverWithScala").setMaster("local[2]")
    val ssc: StreamingContext = new StreamingContext(conf, Seconds(10))
    ssc.sparkContext.setLogLevel("WARN")
    ssc.checkpoint("./kafka_receiver")

    val zkQuorum = "spark-node01.sun.com:2181,spark-node02.sun.com:2181,spark-node03.sun.com:2181"
    val groupId = "spark-receiver_scala"
    //定义topic相关信息
    //这里的value并不是topic的分区数,它表示的topic中每一个分区被N个线程消费
    val topics = Map("kafka_spark" -> 2)

    //KafkaUtils.createDstream(ssc,[zk], [group id], [per-topic,partitions])
    //Kafka中topic的partition与Spark中RDD的partition是没有关系的
    //在createStream()中,设置的持久化级别是StorageLevel.MEMORY_AND_DISK_SER;连接Zk的超时时间是10秒
    val receiverDetream: ReceiverInputDStream[(String, String)] = KafkaUtils.createStream(ssc, zkQuorum, groupId, topics)
    val lines: DStream[String] = receiverDetream.map(x => x._2)
    val pairs: DStream[(String, Int)] = lines.flatMap(_.split(" ")).map(word => (word, 1))
    val wordCounts: DStream[(String, Int)] = pairs.reduceByKey(_ + _)
    wordCounts.print()

    ssc.start()
    ssc.awaitTermination()

  }

}
