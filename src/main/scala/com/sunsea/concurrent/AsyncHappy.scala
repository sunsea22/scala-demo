package com.sunsea.concurrent

import scala.concurrent.{ExecutionContext, Future, Promise, Await}
import scala.concurrent.duration.Duration


/**
 * Created by Flyln on 16/7/6.
 */
object AsyncHappy extends App {
  import ExecutionContext.Implicits.global
  /**
   * 4个任务方法中每一个都为该任务的完成时刻使用可特定的延迟值
   * task1为1秒，task2为2秒，task3为3秒，task4重新变为1秒
   * @param input 输入值
   * @return 输入值加上任务编号作为future的结果值
   */
  def task1(input: Int) = TimeEvent.delayedSuccess(1, input + 1)
  def task2(input: Int) = TimeEvent.delayedSuccess(2, input + 2)
  def task3(input: Int) = TimeEvent.delayedSuccess(3, input + 3)
  def task4(input: Int) = TimeEvent.delayedSuccess(1, input + 4)

  /** 使用阻塞等待，主要线程等待每个任务依次完成 */
  def runBlocking() = {
    val v1: Int = Await.result(task1(1), Duration.Inf)
    val future2 = task2(v1)
    val future3 = task3(v1)
    val v2 = Await.result(future2, Duration.Inf)
    val v3 = Await.result(future3, Duration.Inf)
    val v4 = Await.result(task4(v2 + v3), Duration.Inf)
    val result = Promise[Int]
    result.success(v4)
    result.future
  }

//  /** 不使用阻塞，将future联系在一起 */
//  def runOnSuccess() = {
//    val result = Promise[Int]
//    task1(1).onSuccess(v => v match {
//      case v1 => {
//        val a = task2(v1)
//        val b = task3(v1)
//        a.onSuccess(v => v match {
//          case v2 =>
//            b.onSuccess(v => v match {
//              case v3 => task4(v2 + v3).onSuccess(v4 => v4 match {
//                case x => result.success(x)
//              })
//            })
//        })
//      }
//    })
//    result.future
//  }

  def runFlatMap() = {
    task1(1) flatMap {v1 =>
      val a = task2(v1)
      val b = task3(v1)
      a flatMap { v2 =>
        b flatMap { v3 => task4(v2 + v3)}
      }
    }
  }

  def timeComplete(f: () => Future[Int], name: String) {
    println("Starting " + name)
    val start = System.currentTimeMillis
    val result = Await.result(f(), Duration.Inf)
    val time = System.currentTimeMillis - start
    println(name + " returned " + result + " in " + time + " ms. ")
  }

  timeComplete(runBlocking, "runBlocking")
  //timeComplete(runOnSuccess, "runOnSuccess")
  timeComplete(runFlatMap, "runFlatMap")

  System.exit(0)
}
