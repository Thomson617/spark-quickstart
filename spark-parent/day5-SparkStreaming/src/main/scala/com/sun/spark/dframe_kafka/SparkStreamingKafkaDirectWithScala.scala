package com.sun.spark.dframe_kafka

import kafka.common.TopicAndPartition
import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka.{HasOffsetRanges, KafkaCluster, KafkaUtils, OffsetRange}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Title   
  * Author  SunTao
  * Date  2018/10/29 23:45
  */
object SparkStreamingKafkaDirectWithScala {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("SparkStreamingKafkaDirectWithScala").setMaster("local[2]")
    val ssc: StreamingContext = new StreamingContext(conf, Seconds(10))
    ssc.sparkContext.setLogLevel("WARN")
    ssc.checkpoint("./kafka_direct")

    val kafkaParams = Map("metadata.broker.list" -> "spark-node01.sun.com:9092", "group.id" -> "kafka_direct")
    //采用是kafka低级api,偏移量不受zk管理
    //createDirectStream[K: ClassTag,V: ClassTag,KD <: Decoder[K]: ClassTag,VD <: Decoder[V]: ClassTag] (ssc: StreamingContext,kafkaParams: Map[String, String],topics: Set[String])
    val directDStream: InputDStream[(String, String)] = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topics = Set("kafka_spark"))

    /*Spark内部维护的Kafka偏移量信息是存储在HasOffsetRanges类的offsetRanges中，
     我们可以在Spark Streaming程序里面获取这些信息:
    val offsetsList = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
    调用KafkaCluster类的setConsumerOffsets方法去更新Zookeeper里面的信息,将偏移量重新写入到zk中*/
    directDStream.foreachRDD(rdd => {
      val offsetsList: Array[OffsetRange] = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
      val kc = new KafkaCluster(kafkaParams)
      for (offsets <- offsetsList) {
        val topicAndPartition = TopicAndPartition("iteblog", offsets.partition)
        val o = kc.setConsumerOffsets(args(0), Map((topicAndPartition, offsets.untilOffset)))
        if (o.isLeft) {
          println(s"Error updating the offset to Kafka cluster: ${o.left.get}")
        }
      }
    })


    val lines: DStream[String] = directDStream.map(x => x._2)
    val pairs: DStream[(String, Int)] = lines.flatMap(_.split(" ")).map(word => (word, 1))
    val wordCounts: DStream[(String, Int)] = pairs.reduceByKey(_ + _)
    wordCounts.print()

    ssc.start()
    ssc.awaitTermination()

  }

}
