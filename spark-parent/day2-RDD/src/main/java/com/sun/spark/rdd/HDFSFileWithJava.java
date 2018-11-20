package com.sun.spark.rdd;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;

/**
 * @Title
 * @author SunTao
 * @date 2018/10/22 15:40
 */
public class HDFSFileWithJava {

    public static void main(String[] args) {
        SparkConf sparkConf = new SparkConf().setAppName("HDFSFileWithJava ");
        JavaSparkContext sc = new JavaSparkContext(sparkConf);
        JavaRDD<String> textFile = sc.textFile(args[0]);
        //统计文本文件内的字数
        JavaRDD<Integer> lineLength = textFile.map(new Function<String, Integer>() {
            @Override
            public Integer call(String v1) throws Exception {
                return v1.length();
            }
        });
        //统计文件出现的个数
        Integer count = lineLength.reduce(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer v1, Integer v2) throws Exception {
                return v1 + v2;
            }
        });
        //输出结果
        System.out.println("文件总字数是: "+count);
        //关闭SparkContext
        sc.stop();
    }

}
