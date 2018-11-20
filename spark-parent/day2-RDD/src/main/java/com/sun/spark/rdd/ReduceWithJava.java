package com.sun.spark.rdd;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Arrays;
import java.util.List;

/**
 * @Title
 * @author SunTao
 * @date 2018/10/22 18:17
 */
public class ReduceWithJava {
    public static void main(String[] args) {
        //1:创建SparkConf对象
        SparkConf conf = new SparkConf().setAppName("ReduceWithJava").setMaster("local");
        //2:创建JavaSparkContext
        JavaSparkContext sc = new JavaSparkContext(conf);
        sc.setLogLevel("WARN");
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        JavaRDD<Integer> rdd1 = sc.parallelize(numbers);
        Integer count = rdd1.reduce((a, b) -> a + b);
        System.out.println(count);
    }
}
