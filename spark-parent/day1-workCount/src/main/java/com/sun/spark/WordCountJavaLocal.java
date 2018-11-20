package com.sun.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;

import java.util.Arrays;
import java.util.Iterator;

import scala.Tuple2;

/**
 * @Title
 * @author SunTao
 * @date 2018/10/21 20:19
 */
public class WordCountJavaLocal {

    public static void main(String[] args) {
        //1.创建SparkConf对象,配置Spark应用程序的基本信息
        SparkConf sparkConf = new SparkConf().setAppName("com.sun.spark.WordCountJavaLocal").setMaster("local[2]");

        //解决Could not locate executable null\bin\winutils.exe in the Hadoop binaries.问题
        //如果本地hadoop已设置开机启动,就不需要此步操作
        System.setProperty("hadoop.home.dir", "E:\\software\\hadoop-2.7.4");

        //2.创建JavaSparkContext对象
        JavaSparkContext sc = new JavaSparkContext(sparkConf);

        //3.定义输入源的位置(hdfs文件 或 本地文件),创建一个初始的RDD
        JavaRDD<String> lines = sc.textFile("D:\\workplace\\testFile\\words.txt");
        //4.对初始的RDD进行Transformation操作(计算)
        JavaRDD<String> words = lines.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public Iterator<String> call(String s) throws Exception {
                String[] splits = s.split("\\s+");
                return Arrays.asList(splits).iterator();
            }
        });
        //5.将每一个单词映射成(word,1)这种格式
        JavaPairRDD<String, Integer> pairs = words.mapToPair(new PairFunction<String, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(String word) throws Exception {
                return new Tuple2<String, Integer>(word, 1);
            }
        });
        //6.接着讲单词作为key,统计每个单词出现的次数
        JavaPairRDD<String, Integer> wordCounts = pairs.reduceByKey(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer v1, Integer v2) throws Exception {
                return v1 + v2;
            }
        });
        //7.将结果输出
        wordCounts.foreach(new VoidFunction<Tuple2<String, Integer>>() {
            @Override
            public void call(Tuple2<String, Integer> wordCount) throws Exception {
                System.out.println(wordCount._1 + "  出现了  " + wordCount._2 + "  次");
            }
        });
        //8.释放资源
        sc.stop();
    }

}
