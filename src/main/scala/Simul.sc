trait Summerable[T]{
  def sum(a:T, b:T):T;
}

object SummerInstances {

  implicit val sumInt = new Summerable[Int]{
    def sum(a:Int,b:Int):Int = a + b;
  }
}

implicit object SummerSyntax{
  implicit class SummerOps[T](value:T){
    def |@|(other:T)(implicit summerable:Summerable[T]): T ={
      summerable.sum(value, other)
    }
  }
}

import SummerInstances._
import SummerSyntax._

3 |@| 4


import simulacrum._

@typeclass trait Multiplier[A] {
  @op("|-|") def multiply(a: A, b: A): A

}

implicit val MultiplierInt: Multiplier[Int] = new Multiplier[Int] {
  def multiply(a: Int, b: Int): Int = a * b

}

import Multiplier.ops._

3 |-| 4




