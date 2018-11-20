package com.sun.spark.rdd;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Arrays;
import java.util.List;

import scala.Tuple2;

/**
 * @Title
 * @author SunTao
 * @date 2018/10/22 18:13
 */
public class CogroupWithJava {
    public static void main(String[] args) {
        //1:创建SparkConf对象
        SparkConf conf = new SparkConf().setAppName("CogroupWithJava").setMaster("local");
        //2:创建JavaSparkContext
        JavaSparkContext sc = new JavaSparkContext(conf);
        sc.setLogLevel("WARN");
        //3:准备集合
        List<Tuple2<String, Integer>> list1 = Arrays.asList(new Tuple2<String, Integer>("tom", 1), new Tuple2<String, Integer>("tom", 2), new Tuple2<String, Integer>("jerry", 3), new Tuple2<String, Integer>("kitty", 2));
        List<Tuple2<String, Integer>> list2 = Arrays.asList(new Tuple2<String, Integer>("jerry", 2), new Tuple2<String, Integer>("tom", 1), new Tuple2<String, Integer>("jim", 2));
        //并行化
        JavaPairRDD<String, Integer> rdd1 = sc.parallelizePairs(list1);
        JavaPairRDD<String, Integer> rdd2 = sc.parallelizePairs(list2);
        //
        JavaPairRDD<String, Tuple2<Iterable<Integer>, Iterable<Integer>>> rdd3 = rdd1.cogroup(rdd2);
        System.out.println(rdd3.collect());
    }
}
