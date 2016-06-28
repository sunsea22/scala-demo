package com.sunsea.spark.akka


import akka.actor.{ActorSystem, Props, ActorRef, Actor}
import akka.routing.RoundRobinRouter
import scala.concurrent.duration._


/**
 * 我们要创建的示例应用是使用actor来计算PI的值。计算PI是一项CPU密集的操作，
 * 我们将使用Akka Actor来编写一个可以垂直扩展到多个处理器核上的并发解决方案。
 * 在将来的指南中，这个示例应用将被扩展国使用Akka远程Actor来在水平到集群中的多台机器上。
 */
object Pi extends App{

  calculate(nrOfWorkers = 4, nrOfElements = 10000, nrOfMessages = 10000)

  sealed trait PiMessage
  case object Calculate extends PiMessage

  //从主actor发送给各工作actor，包含工作分配的内容
  case class Work(start: Int, nrOfElements: Int) extends PiMessage

  //从工作actors发送给主actor，包含工作actor的计算结果
  case class Result(value: Double) extends PiMessage

  //从主actor发送给监听器actor，包含pi的最终计算结果和整个计算耗费的时间
  case class PiApproximation(pi: Double, duration: Duration)

  //创建工作actor
  class Worker extends Actor {
    def calculatePiFor(start: Int, nrOfElements: Int):Double = {
      var acc = 0.0
      for(i <- start until(start + nrOfElements))
        acc += 4.0 * (1 - (i % 2) * 2) / (2 * i + 1)
      acc
    }

    def receive = {
      case Work(start, nrOfElements) =>
        sender ! Result(calculatePiFor(start, nrOfElements))
    }
  }

  //创建主actor
  class Master(nrOfWorkers: Int, nrOfMessages: Int, nrOfElements: Int, listener: ActorRef) extends Actor {
    var pi: Double = _
    var nrOfResults: Int = _
    val start: Long = System.currentTimeMillis

    val workerRouter = context.actorOf(Props[Worker].withRouter(RoundRobinRouter(nrOfWorkers)), name = "workerRouter")

    def receive = {
      case Calculate =>
        for (i <- 0 until nrOfMessages) workerRouter ! Work(i * nrOfElements, nrOfElements)
      case Result(value) =>
        pi += value
        nrOfResults += 1
        if (nrOfResults == nrOfMessages) {
          listener ! PiApproximation(pi, duration = (System.currentTimeMillis - start).millis)

          context.stop(self)
        }
    }
  }

  class Listener extends Actor {
    def receive = {
      case PiApproximation(pi, duration) =>
        println("\n\tPi approximation: \t\t%s\n\tCalculation time: \t%s".format(pi, duration))
        context.system.shutdown()
    }
  }

  def calculate(nrOfWorkers: Int, nrOfElements: Int, nrOfMessages: Int) {
    val system = ActorSystem("PiSystem")

    val listener = system.actorOf(Props[Listener], name = "listener")

    val master = system.actorOf(Props(new Master(nrOfWorkers, nrOfMessages, nrOfElements, listener)), name = "master")

    master ! Calculate
  }
}
