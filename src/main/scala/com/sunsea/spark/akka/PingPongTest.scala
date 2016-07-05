package com.sunsea.spark.akka

import akka.actor.{Props, ActorSystem, Actor, ActorRef}


/**
 * Created by Flyln on 16/7/5.
 *涉及到两个actor的交互。 就像两个人在乒乒乓乓的打乒乓球。
 * 两个actor来回的ping pang，直到达到特定的次数才停止。
 *
 * Ping 接收StartMessage和 PongMessage。
 * StartMessage是一个启动消息，由main对象发送，PongMessage来自Pong actor，如果次数还未达到，它继续发送PingMessage。
 *
 * Pong 接收StopMessage和 PingMessage。 如果接收到PingMessage，它就发送一个PongMessage, 如果是StopMessage， 停止ActorSystem
 */

case object PingMessage
case object PongMessage
case object StartMessage
case object StopMessage

class Ping(pong: ActorRef) extends Actor {
  var count = 0
  def incrementAndPrint {
    count += 1
    println("ping")
  }

  def receive = {
    case StartMessage => incrementAndPrint
      pong ! PingMessage
    case PongMessage =>
      if (count > 9) {
        sender ! StopMessage
        println("ping stopped")
        context.stop(self)
      }
      else {
        incrementAndPrint
        sender ! PingMessage
      }
  }
}

class Pong extends Actor {
  def receive = {
    case PingMessage =>
      println(" pong")
      sender ! PongMessage
    case StopMessage =>
      println("pong stopped")
      context.stop(self)
      context.system.shutdown()
  }
}

object PingPongTest extends App{
  val system = ActorSystem("PingPongSystem")
  val pong = system.actorOf(Props[Pong], name = "pong")
  val ping = system.actorOf(Props(new Ping(pong)), name = "ping")

  ping ! StartMessage
}
