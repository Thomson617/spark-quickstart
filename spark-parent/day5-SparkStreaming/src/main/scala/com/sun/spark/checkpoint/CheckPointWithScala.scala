package com.sun.spark.checkpoint

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Title   
  * Author  SunTao
  * Date  2018/11/1 15:31
  */
object CheckPointWithScala {
  def main(args: Array[String]): Unit = {
    //如果 checkpointDirectory 存在，那么ssc将导入 checkpoint 数据。
    // 如果目录不存在，函数 functionToCreateContext 将被调用并创建新的 ssc
    val ssc = StreamingContext.getOrCreate("/checkpoint", functionToCreateContext _)
    ssc.sparkContext.setLogLevel("WARN")
    // 启动流计算
    ssc.start()
    ssc.awaitTermination()
  }

  // 通过函数来创建或者从已有的checkpoint里面构建StreamingContext
  def functionToCreateContext(): StreamingContext = {
    val conf: SparkConf = new SparkConf().setAppName("CheckPointWithScala")
    val batchDuration = 5
    val ssc: StreamingContext = new StreamingContext(conf, Seconds(batchDuration))
    val rdds = ssc.socketTextStream("192.168.200.151", 9000) // create DStreams

    // 设置在HDFS上的checkpoint目录
    ssc.checkpoint("/checkpoint")
    // 设置通过间隔时间，定时持久checkpoint到hdfs上
    rdds.checkpoint(Seconds(batchDuration * 5))
    rdds.foreachRDD(rdd => {
      //可以针对rdd每次调用checkpoint //注意上面设置了，定时持久checkpoint下面这个地方可以不用写
      rdd.checkpoint()
    }) //返回ssc
    ssc
  }
}

