package com.sun.spark.rdd;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Arrays;
import java.util.List;

/**
 * @Title
 * @author SunTao
 * @date 2018/10/22 19:35
 */
public class Repartition_CoalesceWithJava {
    /*
     * repartition可以增加和减少rdd中的分区数，coalesce只能减少rdd分区数，增加rdd分区数不会生效
     */
    public static void main(String[] args) {
        //1:创建SparkConf对象
        SparkConf conf = new SparkConf().setAppName("Repartition_CoalesceWithJava").setMaster("local");
        //2:创建JavaSparkContext
        JavaSparkContext sc = new JavaSparkContext(conf);
        sc.setLogLevel("WARN");
        //3:准备集合
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        JavaRDD<Integer> rdd = sc.parallelize(numbers, 3);
        //4:查看分区大小
        System.out.println(rdd.partitions().size());
        //5:利用repartition改变rdd的分区大小
        System.out.println(rdd.repartition(2).partitions().size());
        System.out.println(rdd.repartition(4).partitions().size());
        //利用coalesce改变rdd分区数
        //减少分区
        System.out.println(rdd.coalesce(2).partitions().size());
        System.out.println(rdd.coalesce(4).partitions().size());  //结果还是3
    }
}
