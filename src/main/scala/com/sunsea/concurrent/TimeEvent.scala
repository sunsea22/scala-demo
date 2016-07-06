package com.sunsea.concurrent

import java.util.{TimerTask, Timer}

import scala.concurrent.{Promise, Future}

/**
 * Created by Flyln on 16/7/6.
 */
object TimeEvent {
  val timer = new Timer

  /**
   *定制了一个任务，在运行成功时完成一个scala Future[T]
   * @param secs
   * @param value
   * @tparam T
   * @return
   */
  def delayedSuccess[T](secs: Int, value: T): Future[T] = {
    val result = Promise[T]
    timer.schedule(new TimerTask {
      override def run(): Unit = result.success(value)
    }, secs * 1000)
    result.future
  }

  def delayedFailure(secs: Int, msg: String):Future[Int] = {
    val result = Promise[Int]
    timer.schedule(new TimerTask {
      override def run(): Unit = result.failure(new IllegalArgumentException(msg))
    },secs * 1000)
    result.future
  }

}
