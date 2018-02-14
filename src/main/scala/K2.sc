import cats.data.Ior
import cats.arrow.Arrow
import cats.data.Ior
import cats.kernel.{Eq, Semigroup}
import com.company.ExceptionCombinator.{Node, Portfolio}
sealed trait ExceptionCombinator
object ExceptionCombinator{
  implicit val exceptionCombinator: Semigroup[Exception] =new Semigroup[Exception]{
    override def combine(x: Exception, y: Exception) = x
  }
  implicit def eqTree: Eq[Exception] = Eq.fromUniversalEquals
case class Portfolio(p:String)
case class PortfolioE(portfolio:Portfolio)
case class Node(node:String)
case class NodeE(node:Node)
case class Result(r:String)
case class Response(result:Result)
type ExOr[R] = Ior[Exception,R]
type Ex = Exception
val ntoEntity: (Node, Portfolio) => Right[Ex, NodeE] = (n:Node, p:Portfolio) =>Right[Ex,NodeE](NodeE(n))
val ptoEntity: Portfolio => Either[Ex, PortfolioE] = (p:Portfolio) => Ior.right[Ex, PortfolioE](PortfolioE(p)).toEither
val compute: (NodeE, PortfolioE) => Either[Ex, Result] = (ne:NodeE, pe:PortfolioE) => Ior.right[Ex,Result](Result("result")).toEither
val renderResponse: Result => Either[Ex, Response] = (r:Result) => Ior.right[Ex,Response](Response(r)).toEither
val portfolio = Portfolio("porftolio")
val node = Node("node")
for{

  nodeE <- ntoEntity(node, portfolio)
  portfolioE <- ptoEntity(portfolio)
  result <- compute(nodeE,portfolioE)
  response <- renderResponse(result)


} yield response


import cats.arrow.Arrow
import cats.data.{Ior, Kleisli}
import cats.implicits._
import com.company.ExceptionCombinator.combine

def combine[F[_, _]: Arrow, A, B, C](fab: F[A, B], fac: F[A, C]): F[A, (B, C)] =
  Arrow[F].lift((a: A) => (a, a)) >>> (fab *** fac)

case class Request(n:Node, p:Portfolio)
val r2Node:Request => Right[Ex, NodeE] = (r:Request) =>Right[Ex,NodeE](NodeE(r.n))
val r2Portfolio: Request => Right[Ex,PortfolioE] = (r:Request) => Right[Ex,PortfolioE](PortfolioE(r.p))

val r2NodeK = Kleisli(r2Node)
val r2PortfolioK = Kleisli(r2Portfolio)

val combined = combine(r2NodeK,r2PortfolioK)
