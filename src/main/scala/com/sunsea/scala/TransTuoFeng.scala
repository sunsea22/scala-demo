package com.sunsea.scala

import java.util


/**
 * Created by Flyln on 16/7/20.
 * 驼峰字符串的转换
 */
object TransTuoFeng {
  def main(args: Array[String]) {
    val tuoFeng = "HelloWorld"
    println(trans(tuoFeng))

  }

  def trans(str: String): String = {
    val record = new util.ArrayList[Int]()

    for (i <- 0 until str.length) {
      val tmp = str.charAt(i)

      if (tmp <= 'Z' && tmp >= 'A') {
        record.add(i) //记录每个大写字母的位置
      }
    }
    record.remove(0)

    val str1 = str.toLowerCase //将字符串转换成小写字母
    val charOfStr = str1.toCharArray
    val t = new Array[String](record.size())

    for (i <- 0 until record.size()) {
      t(i) = "_" + charOfStr(record.get(i))
    }
    var result = ""
    var flag = 0

    for (i <- 0 until(str1.size)) {
      if (flag < record.size() && i == record.get(flag)) {
        result += t(flag)
        flag += 1
      }
      else result += charOfStr(i)
    }
    result
  }

}
