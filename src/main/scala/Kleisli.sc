import cats.Applicative
import cats.arrow.Arrow
import cats.data.{Ior, Kleisli}
import cats.instances.all._
import cats.syntax.all._

val addEmpty: Int => Int = _ + 0
val multiplyEmpty: Int => Double= _ * 1d
val f: Int => (Int, Double) = addEmpty &&& multiplyEmpty

val fM = (x:Int) => if(x>10) Right[Ex,Int](x) else Left[Ex,Int](new Exception("min10"))




Kleisli(fM)
case class Request(n:Node, p:Portfolio)

case class Portfolio(p:String)
case class PortfolioE(portfolio:Portfolio)
case class Node(node:String)
case class NodeE(node:Node)
case class Result(r:String)
case class Response(result:Result)
type ExOr[R] = Ior[Exception,R]
type ExEr[R] = Either[Exception,R]
type Ex = Exception
val ntoEntity: Request => ExOr[NodeE] = (r:Request) => Ior.right[Ex, NodeE](NodeE(r.n))
val pRequest = (r:Request)=> r.p
//val ptoEntity: (Portfolio) => ExOr[PortfolioE] = (p:Portfolio) => Ior.right[Ex, PortfolioE](PortfolioE(p))

val ptoEntity2: Portfolio => ExOr[PortfolioE] = (p:Portfolio) => Ior.right[Ex, PortfolioE](PortfolioE(p))
val ptoEntity: Request => ExOr[PortfolioE] = pRequest.andThen(ptoEntity2)
val compute: (NodeE, PortfolioE) => ExOr[Result] = (ne:NodeE, pe:PortfolioE) => Ior.right[Ex,Result](Result("result"))
val renderResponse: Result => ExOr[Response] = (r:Result) => Ior.right[Ex,Response](Response(r))
val portfolio = Portfolio("porftolio")
val node = Node("node")
//val res = for{
//
//  nodeE <- ntoEntity(node, portfolio)
//  portfolioE <- ptoEntity(portfolio)
//  result <- compute(nodeE,portfolioE)
//  response <- renderResponse(result)
//
//
//} yield response

import cats.data.Kleisli
import cats.instances.all._
import cats.syntax.eitherK
import cats.instances.EitherInstances
import cats.Monad


implicit def eitherMonad[Err]: Monad[Either[Err, ?]] =
  new Monad[Either[Err, ?]] {
    def flatMap[A, B](fa: Either[Err, A])(f: A => Either[Err, B]): Either[Err, B] =
      fa.flatMap(f)

    def pure[A](x: A): Either[Err, A] = Either.right(x)

    @annotation.tailrec
    def tailRecM[A, B](a: A)(f: A => Either[Err, Either[A, B]]): Either[Err, B] =
      f(a) match {
        case Right(Right(b)) => Either.right(b)
        case Right(Left(a)) => tailRecM(a)(f)
        case l@Left(_) => l.rightCast[B] // Cast the right type parameter to avoid allocation
      }
  }
val pint= (x:Int)=> if(x<10) Right(x) else Left(new Exception(""))

val k = Kleisli(ntoEntity)
val k2 = Kleisli(pint)
val k3 = Kleisli.liftF(pint)
val k4 = Kleisli(ptoEntity)
(ntoEntity).merge(ptoEntity)
(Kleisli(ntoEntity))

def merge[F[_],A,B,C](fab:Kleisli[F[_],A,B], fac: Kleisli[F[_],A,C]): A => F[_][B] ={
  (a:A)=>fab.run(a)
}






