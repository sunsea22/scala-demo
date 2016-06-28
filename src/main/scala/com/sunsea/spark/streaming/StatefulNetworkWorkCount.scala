package com.sunsea.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{StateSpec, State, Seconds, StreamingContext}

/**
 * Created by Sunsea on 16/6/28.
 * 保持一个文本数据流中每个单词的运行次数
 * 每次跟新后的状态是在前面状态的基础上进行跟新的
 */
object StatefulNetworkWorkCount {
  def main(args: Array[String]) {
//    if (args.length < 2) {
//      System.err.println("Usage: StatefulNetworkWordCount <hostname> <port>")
//      System.exit(1)
//    }

    StreamingExample.setStreamingLogLevels()

    val sparkConf = new SparkConf().setMaster("local").setAppName("StatefulNetworkWordCount")
    val ssc = new StreamingContext(sparkConf, Seconds(1))
    ssc.checkpoint(".")

    val initialRDD = ssc.sparkContext.parallelize(List(("hello",1),("world",1)))

    val lines = ssc.socketTextStream("localhost",9999)
    val words = lines.flatMap(_.split(" "))
    val wordDstream = words.map(x => (x,1))

    val mappingFunc = (word: String, one: Option[Int], state: State[Int]) => {
      val sum = one.getOrElse(0) + state.getOption().getOrElse(0)
      val output = (word, sum)
      state.update(sum)
      output
    }

    val stateDstream = wordDstream.mapWithState(StateSpec.function(mappingFunc).initialState(initialRDD))
    stateDstream.print()
    ssc.start()
    ssc.awaitTermination()

  }

}
