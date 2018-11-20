package com.sun.spark.rdd;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Arrays;
import java.util.List;

import scala.Tuple2;

/**
 * @Title
 * @author SunTao
 * @date 2018/10/22 17:53
 */
public class Join_GroupByKeyWithJava {
    public static void main(String[] args) {
        SparkConf sparkConf = new SparkConf().setAppName("Join_GroupByKeyWithJava ").setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(sparkConf);
        sc.setLogLevel("WARN");

        List<Tuple2<String, Integer>> list1 = Arrays.asList(new Tuple2<String, Integer>("tom", 1), new Tuple2<String, Integer>("jerry", 3), new Tuple2<String, Integer>("kitty", 2));
        List<Tuple2<String, Integer>> list2 = Arrays.asList(new Tuple2<String, Integer>("jerry", 2), new Tuple2<String, Integer>("tom", 1), new Tuple2<String, Integer>("rose", 2));

        JavaPairRDD<String, Integer> rdd1 = sc.parallelizePairs(list1);
        JavaPairRDD<String, Integer> rdd2 = sc.parallelizePairs(list2);

        //求join
        JavaPairRDD<String, Tuple2<Integer, Integer>> rdd3 = rdd1.join(rdd2);
        //求并集
        JavaPairRDD<String, Integer> rdd4 = rdd1.union(rdd2);
        System.out.println("rdd4--> " + rdd4.collect());
        JavaPairRDD<String, Iterable<Integer>> rdd5 = rdd4.groupByKey();
        System.out.println("rdd5--> " + rdd5.collect());


        //按照key进行聚合
        JavaPairRDD<String, Integer> rdd6 = rdd4.reduceByKey((a, b) -> a + b);
        //将rdd4收集到一个集合中
        System.out.println("rdd6--> " + rdd6.collect());
        //按照value的值进行降序排序
        JavaRDD<Tuple2> result = rdd6.map(value -> new Tuple2(value._2, value._1)).sortBy(value -> value._1, false, 1).map(value -> new Tuple2(value._2, value._1));
        System.out.println("result--> " + result.collect());


    }
}
