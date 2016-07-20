package com.sunsea.scala

import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success, Random}
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * 被推选的政客给他的投票者一个减税的承诺的例子
 */

case class TaxCut(reduction: Int) {
  println("reducing start now")
  Thread.sleep(Random.nextInt(200))
  println("reducing stop now")
}

object Government {
  val p = Promise[TaxCut]()
  val f = p.future
  //Promise的完成和对返回的Future的处理发生在不同的线程
  def redeemCampaignPledge() = Future {
    println("Starting the new legislative period.")

    Thread.sleep(Random.nextInt(200))
    p.success(TaxCut(20))

    Thread.sleep(Random.nextInt(200))
    println("We reduce the taxes! You must reelect us!!!1111")
  }
}
object FutureAndPromise {

  def main(args: Array[String]) {
    Government.redeemCampaignPledge()
    val taxCutF: Future[TaxCut] = Government.f
    println("Now that they're elected, let's see if they remember their promises...")
    taxCutF.onComplete {
      case Success(TaxCut(reduction)) =>
        println(s"A miracle! They really cut our taxes by $reduction percentage points")
      case Failure(ex) =>
        println(s"They broke their promises! Again! because of a ${ex.getMessage}")
    }
    Thread.sleep(1000)
  }
}
