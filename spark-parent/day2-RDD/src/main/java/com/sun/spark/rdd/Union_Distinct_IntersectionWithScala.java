package com.sun.spark.rdd;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Arrays;
import java.util.List;

/**
 * @Title
 * @author SunTao
 * @date 2018/10/22 17:39
 */
public class Union_Distinct_IntersectionWithScala {
    public static void main(String[] args) {

        //1:创建SparkConf对象
        SparkConf conf = new SparkConf().setAppName("Union_Distinct_IntersectionWithJava").setMaster("local");
        //2:创建JavaSparkContext
        JavaSparkContext sc = new JavaSparkContext(conf);

        sc.setLogLevel("WARN");
        //3:准备集合
        List<Integer> list1 = Arrays.asList(5, 6, 4, 3);
        List<Integer> list2 = Arrays.asList(1, 2, 3, 4);

        //4:并行化集合,获取初始RDD
        JavaRDD<Integer> rdd1 = sc.parallelize(list1);
        JavaRDD<Integer> rdd2 = sc.parallelize(list2);

        //求并集
        JavaRDD<Integer> rdd3 = rdd1.union(rdd2);
        System.out.println("rdd3--> "+rdd3.collect());
        //求交集
        JavaRDD<Integer> rdd4 = rdd1.intersection(rdd2);
        System.out.println("rdd4--> "+rdd4.collect());
        //去重
        JavaRDD<Integer> rdd5 = rdd3.distinct();
        System.out.println("rdd5--> "+ rdd5.collect());

    }

}
