import scala.language.higherKinds

trait Functor[F[_]]{
  def map[A,B](fa:F[A])(f: A => B): F[B]
}

import cats.Functor
import cats.instances.list._
import cats.instances.option._

val list = List(1,2,3)

Functor[List].map(list)(_*2)

val option = Option(123)

Functor[Option].map(option)(_.toString)

val func = (x:Int) => x + 1
val f1 = Functor[Option].lift(func)



val func1 = (a: Int) => a + 1
// func1: Int => Int = <function1>
val func2 = (a: Int) => a * 2
// func2: Int => Int = <function1>
val func3 = func1.compose(func2)


sealed trait Tree[+A]
final case class Branch[A](left: Tree[A], right: Tree[A])
  extends Tree[A]
final case class Leaf[A](value: A) extends Tree[A]

def leaf[A](value:A):Tree[A]=Leaf(value)
def branch[A](left:Tree[A],right:Tree[A]):Tree[A] = Branch(left, right)

implicit val treeFunctor = new Functor[Tree]{
  override def map[A, B](fa: Tree[A])(f: A => B): Tree[B] = {
    fa match {
      case Branch(left:Tree[A], right: Tree[A]) => Branch(map(left)(f),
        map(right)(f))
      case Leaf(value:A) => Leaf(f(value))
    }
  }
}
Option(1).map(_ + 2).map(_ * 3).map(_ + 100)












