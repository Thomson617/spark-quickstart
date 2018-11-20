package com.sun.spark.rdd;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Arrays;
import java.util.List;

/**
 * @Title
 * @author SunTao
 * @date 2018/10/22 17:05
 */
public class FlatMapWithJava {
    public static void main(String[] args) {

        SparkConf conf = new SparkConf().setAppName("MapFilterWithJava").setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);
        List<String> list = Arrays.asList("hello word", "hello hadoop", "hello spark hadoop hive");
        JavaRDD<String> rdd = sc.parallelize(list);
        //切分每一行数据
        JavaRDD<String> words = rdd.flatMap(line -> Arrays.asList(line.split(" ")).iterator());

        List<String> collect = words.collect();
        System.out.println(collect);
        sc.stop();
    }

}
