package com.sunsea

/**
 * Created by Flyln on 16/6/2.
 */
object CovariantAndContravariant {
  def main(args: Array[String]) {


    class Animal {println("Animal")}
    class Bird extends Animal {println("Bird")}

    //协变
    println("===========协变===========")
    class Convariant[+T](t:T){}
    val cov = new Convariant[Bird](new Bird)
    val cov2: Convariant[Animal] = cov

    //逆变
    println("===========逆变===========")
    class Contravariant[-T](t:T){}
    val c = new Contravariant[Animal](new Animal)
    val c2: Contravariant[Bird] = c

    //上界
    println("============上界===========")
    class UpperBoundAnimal{println("UpperBoundAnimal")}
    class UpperBoundBird extends UpperBoundAnimal{println("UpperBoundBird")}
    class UpperBoundBlueBird extends UpperBoundBird{println("UpperBoundBlueBird")}
    class UpperBound[-T](t: T){
      def use[S <: T](s: S){println("use")}
    }
    val upper = new UpperBound[UpperBoundAnimal](new UpperBoundAnimal)
    val upper2: UpperBound[UpperBoundBird] = upper
    upper2.use(new UpperBoundBird)
    upper.use(new UpperBoundBird)
    upper2.use(new UpperBoundBlueBird)
    upper.use(new UpperBoundBird)

    //下界
    println("==========下界=============")
    class LowerBoundAnimal(){println("LowerBoundAnimal")}
    class LowerBoundBird extends LowerBoundAnimal(){println("LowerBoundBird")}
    class LowerBoundBlueBird extends LowerBoundBird(){println("LowerBoundBlueBird")}
    class LowerBound[+T](t:T) {
      def use[S >: T](s: S){println("use")}
    }
    val lower = new LowerBound[LowerBoundBird](new LowerBoundBlueBird)
    val lower2:LowerBound[LowerBoundBird] = lower
    lower2.use(new LowerBoundAnimal)
    lower2.use(new LowerBoundBird)

    lower2.use(new LowerBoundBlueBird)
  }

}
