package com.sunsea.scala

/**
 * 高阶函数
 * 实现一个免费的邮件服务，用户可以设置对邮件的屏蔽
 */

case class Email(subject: String, text: String, sender: String, recipient: String)

object Email {
  type EmailFilter = Email => Boolean

  def newMailForUser(mail: Seq[Email], f: EmailFilter) = mail.filter(f)

  object EmailFilterFactory {
    //谓词函数
    def complement[A](predicate: A => Boolean) = (a: A) => !predicate(a)

    val sentByOneOf: Set[String] => EmailFilter = senders => email => senders.contains(email.sender)

    val notSentByAnyOf = sentByOneOf andThen(complement(_))

    type SizeChecker = Int => Boolean

    val sizeConstraint: SizeChecker => EmailFilter = f => email => f(email.text.length)

    val minimumSize: Int => EmailFilter = n => sizeConstraint(_ >= n)

    val maximumSize: Int => EmailFilter = n => sizeConstraint(_ <= n)
  }
}

object HighOrderFunction {
  def main(args: Array[String]) {
    val emailFilter: Email.EmailFilter = Email.EmailFilterFactory.notSentByAnyOf(Set("1234@example.com"))
    val mails = Email(
      subject = "Its me again, your stalker friend!",
      text = "Hello my friend! How are you?",
      sender = "12345@example.com",
      recipient = "me@example.com") :: Nil
    Email.newMailForUser(mails, emailFilter)
  }
}
