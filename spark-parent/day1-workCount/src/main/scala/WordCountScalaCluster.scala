import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Title   
  * Author  SunTao
  * Date  2018/10/21 23:00
  */
object WordCountScalaCluster {

  def main(args: Array[String]): Unit = {
    //1:创建SparkConf对象
    val sparkConf = new SparkConf().setAppName("WordCountScalaCluster")
    //2:创建SparkContext对象
    val sc: SparkContext = new SparkContext(sparkConf)
    //3:读取文件
    val lines: RDD[String] = sc.textFile(args(0))
    //4:将每一行数据切分为单词
    val words: RDD[String] = lines.flatMap(line => line.split(" "))
    //5:将出现的每一个单词记为1
    val pairs: RDD[(String, Int)] = words.map(word => (word, 1))
    //6:统计相同的单词出现的次数
    val result: RDD[(String, Int)] = pairs.reduceByKey((a, b) => a + b)
    //7:遍历输出所有的结果
    result.saveAsTextFile(args(1))
    //8:释放资源
    sc.stop()
  }

}
