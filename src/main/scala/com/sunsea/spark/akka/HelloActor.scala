package com.sunsea.spark.akka

import akka.actor.{Props, ActorSystem, Actor}

/**
 * Created by Flyln on 16/7/5.
 * 一个简单的akka actor例子
 */

class HelloTest extends Actor {
  def receive = {
    case "hello" => println("您好！")
    case _       => println("您是？")
  }
}

object HelloActor extends App {
  val system = ActorSystem("HelloSystem")
  //缺省的Actor构造函数
  val helloActor = system.actorOf(Props[HelloTest], name = "helloactor")
  helloActor ! "hello"
  helloActor ! "喂"
}
