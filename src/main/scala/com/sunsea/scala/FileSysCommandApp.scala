package com.sunsea.scala

import java.net.URL

import scala.io.Source

/**
 * 文件系统操作
 */
object FileSysCommandApp {
  def main(args: Array[String]) {
    val source = Source.fromURL("http://www.baidu.com", "UTF-8")
    println(source.mkString)

    import sys.process._
    "ls -la ." !
    val result = "ls -l ." #| "grep README" #| "wc -l" !!

    println(result)
    "grep baidu" #< new URL("http://www.baidu.com")
  }

}
