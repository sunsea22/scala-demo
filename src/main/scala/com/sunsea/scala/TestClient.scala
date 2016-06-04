package com.sunsea.scala

/**
 * 测试trait
 */
object TestClient {
  def main(args: Array[String]) {
    val queue1 = new BasicIntQueue with Incrementing with Doubling
    queue1.put(2)   //2*2 => 4 +1
    println(queue1.get())

    val queue2 = new BasicIntQueue with Doubling with Incrementing
    queue2.put(2) //2+1 => 3*2
    println(queue2.get())
  }

  abstract class IntQueue {
    def get(): Int
    def put(x :Int)
  }

  class BasicIntQueue extends IntQueue {
    private val buf = new scala.collection.mutable.ArrayBuffer[Int]
    def get() = buf.remove(0)
    def put(x: Int) {buf += x}
  }

  trait Incrementing extends IntQueue {
    abstract override def put(x: Int) {
      super.put(x + 1)
    }
  }

  trait Doubling extends IntQueue {
    abstract override def put(x: Int): Unit = {
      super.put(2 * x)
    }
  }

}
