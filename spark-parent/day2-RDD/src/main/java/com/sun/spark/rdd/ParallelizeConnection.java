package com.sun.spark.rdd;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;

import java.util.Arrays;
import java.util.List;

/**
 * @Title
 * @author SunTao
 * @date 2018/10/22 14:13
 */
public class ParallelizeConnection {
    public static void main(String[] args) {
        SparkConf sparkConf = new SparkConf().setAppName("ParallelizeConnection").setMaster("local[2]");
        JavaSparkContext sc = new JavaSparkContext(sparkConf);
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        //通过并行化集合的方式创建RDD,需要调用SparkContext以及其子类的parallelize()方法
        JavaRDD<Integer> numberRDD = sc.parallelize(numbers);
        //执行reduce算子操作,相当于先进行1+2=3,然后再用3+3=6,然后再用6 + 4 = 10......以此类推
        Integer sum = numberRDD.reduce(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer v1, Integer v2) throws Exception {
                return v1 + v2;
            }
        });
        System.out.println("1~10之间的累加和: " + sum);
        sc.stop();

    }

}
