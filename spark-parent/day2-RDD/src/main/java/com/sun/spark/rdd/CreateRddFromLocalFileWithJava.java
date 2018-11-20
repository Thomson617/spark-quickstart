package com.sun.spark.rdd;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * @Title
 * @author SunTao
 * @date 2018/10/22 15:14
 */
public class CreateRddFromLocalFileWithJava {

    public static void main(String[] args) {
        SparkConf sparkConf = new SparkConf().setAppName("CreateRddFromLocalFileWithJava ").setMaster("local[2]");
        JavaSparkContext sc = new JavaSparkContext(sparkConf);
        //读取本地文件,得到初始的RDD
        JavaRDD<String> lines = sc.textFile("D:\\workplace\\testFile\\testFile1.txt");
        //获取每一行内容的长度,其实就是每一行内容的字数
        JavaRDD<Integer> lineLength = lines.map(line -> line.length());
        //对每一行的字数进行求和,第一行的字数+第二行的字数=和,和+第三行的字数,以此类推,得到总的字数
        Integer count = lineLength.reduce((a, b) -> a + b);
        //6:将总的字数输出到控制台
        System.out.println("这个文件中的总的字数为:"+count);
        sc.stop();
    }

}
