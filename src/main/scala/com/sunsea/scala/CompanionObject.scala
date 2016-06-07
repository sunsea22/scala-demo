package com.sunsea.scala

/**
 * 伴生对象
 * 当单例对象与某个类共享同一个名称时，他就被称作是这个类的伴生对象
 */
class ChecksumAccumulator {
  private var sum = 0
  def add(b: Byte): Unit = {
    sum += b
  }
  def checksum(): Int = ~(sum & 0xFF) + 1
}

object ChecksumAccumulator {
  private val cache = scala.collection.mutable.Map[String, Int]()
  def calculate(s: String): Int = {
    if (cache.contains(s))
      cache(s)
    else {
      val acc = new ChecksumAccumulator
      for (c <- s) acc.add(c.toByte)
      val cs = acc.checksum()
      cache += (s -> cs)
      cs
    }
  }
}
object CompanionObject {
  def main(args: Array[String]) {
    println(ChecksumAccumulator.calculate("Every value is an object."))
  }
}
