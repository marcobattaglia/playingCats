import cats.data.Ior
import cats.data.Ior.{left, right}

import scala.language.higherKinds
import scala.util.{Failure, Success, Try}

type ExIorL[E] = Exception Ior List[E]

trait FoldError[List[_]]{
  def foldLeftE[A](l: List[A],f:A => Exception Ior A): Exception Ior List[A]
}
object FoldErrorInstances {

  implicit val listIntError = new FoldError[List] {
    def foldLeftE[E](l: List[E], f: E => Exception Ior E): Exception Ior List[E] = {
      l.foldLeft[Exception Ior List[E]](right(List[E]())) { (prev: Exception Ior List[E], curr:E) =>
        prev match {
          case e: Ior.Left[Exception] => left(e.a)
          case r: Ior.Right[List[E]] => {

            f(curr) match {
              case e: Ior.Left[Exception] => left(e.a)
              case x: Ior.Right[E] => right(new scala.collection.immutable.::(x.b,r.b))
            }
          }
        }
      }
    }
  }
}

implicit object FoldErroSyntax{
  implicit class FoldErrorOps[T](value:List[T]){
    def @*>(f: T => Exception Ior T)(implicit foldError:FoldError[List]): Ior[Exception, List[T]] ={
      foldError.foldLeftE[T](value, f)
    }
  }
}

import FoldErrorInstances._
import FoldErroSyntax._
val c = List(1,2,3,4,5,0,6)
val func=(x:Int) => Try{  x/x} match {
case Success(r:Int) => right(r)
case Failure(e:Throwable) => left(new Exception("failure"))
}

c @*> func
Some(1).flatMap((x:Int) => Some(x+1))

import cats.instances.all._
import cats.syntax.flatMap._

List(1) >>=  {(x:Int) => List(x+1)}
Option(1) >>= {(x:Int) => Option(1)}
Option(1) >>= {(x:Int) => Some(1)}
right[Exception,Int](1) flatMap {(x:Int) => right[Exception,Int](1)}