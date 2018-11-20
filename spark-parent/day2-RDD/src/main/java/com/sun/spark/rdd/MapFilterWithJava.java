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
public class MapFilterWithJava {
    public static void main(String[] args) {

        SparkConf conf = new SparkConf().setAppName("MapFilterWithJava").setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);
        List<Integer> numbers = Arrays.asList(5, 6, 4, 7, 3, 8, 2, 9, 1, 10);
        JavaRDD<Integer> rdd = sc.parallelize(numbers);
        JavaRDD<Integer> sortByRDD = rdd.map(num -> num * 2).sortBy(n -> n, true, 1);
        JavaRDD<Integer> filter = sortByRDD.filter(num -> num > 10);
        List<Integer> collect = filter.collect();
        System.out.println(collect);
        sc.stop();
    }

}
