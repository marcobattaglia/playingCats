import cats.{Applicative, Traverse}
import cats.instances.list._
import cats.syntax.traverse._
import cats.data.Ior
import cats.data.Ior.{left, right=>ioright}
import cats.instances._


import scala.language.higherKinds
import scala.util.{Failure, Success, Try}

type ExIorL[E] = Exception Ior List[E]

val l = List[Int](1,2,3,4)

val func: Int => Ior[Exception, Int] = (x:Int) => Try{  x/x} match {
  case Success(r:Int) => ioright(r)
  case Failure(e:Throwable) => left(new Exception("failure"))
}




def traverseIor[E, A, B](as: List[A])(f: A => Ior[E, B]): Ior[E, List[B]] =
  as.foldRight(Ior.Right(List.empty[B]): Ior[E, List[B]]) { (a: A, acc: Ior[E, List[B]]) =>
    val iorB: Ior[E, B] = f(a)
    Applicative[Ior[E, ?]].map2(iorB, acc)(_ :: _)
  }

traverseIor[Exception,Int,Int](l)(func)
