import cats.data.Ior
import cats.data.Ior.{left, right}
import cats.kernel.Semigroup

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

import FoldErroSyntax._
import FoldErrorInstances._
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

implicit val exceptionCombinator: Semigroup[Exception] =new Semigroup[Exception]{
  override def combine(x: Exception, y: Exception) = x
}

case class Portfolio(p:String)
case class PortfolioE(portfolio:Portfolio)
case class Node(node:String)
case class NodeE(node:Node)
case class Result(r:String)
case class Response(result:Result)
type ExOr[R] = Ior[Exception,R]
type ExEr[R] = Either[Exception,R]
type Ex = Exception
val ntoEntity: (Node, Portfolio) => ExOr[NodeE] = (n:Node, p:Portfolio) => Ior.right[Ex, NodeE](NodeE(n))
val ptoEntity: (Portfolio) => ExOr[PortfolioE] = (p:Portfolio) => Ior.right[Ex, PortfolioE](PortfolioE(p))
val ptoEntity2= (p:Portfolio) => Right(PortfolioE(p))
val compute: (NodeE, PortfolioE) => ExOr[Result] = (ne:NodeE, pe:PortfolioE) => Ior.right[Ex,Result](Result("result"))
val renderResponse: Result => ExOr[Response] = (r:Result) => Ior.right[Ex,Response](Response(r))
val portfolio = Portfolio("porftolio")
val node = Node("node")
val res = for{

  nodeE <- ntoEntity(node, portfolio)
  portfolioE <- ptoEntity(portfolio)
  result <- compute(nodeE,portfolioE)
  response <- renderResponse(result)


} yield response

import cats.data.Kleisli
import cats.instances.all._
import cats.syntax.all._

val addEmpty: Int => Int = _ + 0
val multiplyEmpty: Int => Double= _ * 1d
val f: Int => (Int, Double) = addEmpty &&& multiplyEmpty
val k = Kleisli.liftF(ntoEntity)
  val id = Kleisli.liftF((n:Node,p:Portfolio)=> Ior.right[Ex,(Node,Portfolio)](n,p))
val k2 = Kleisli[Either[Ex,PortfolioE],Portfolio,ExEr[PortfolioE]](ptoEntity2)

