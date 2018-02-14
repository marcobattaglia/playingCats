//package com.company
//
//import cats.arrow.Arrow
//import cats.data.Ior
//import cats.kernel.{Eq, Semigroup}
//import com.company.ExceptionCombinator.{Node, Portfolio}
//sealed trait ExceptionCombinator
//object ExceptionCombinator{
//  implicit val exceptionCombinator: Semigroup[Exception] =new Semigroup[Exception]{
//    override def combine(x: Exception, y: Exception) = x
//  }
//  implicit def eqTree: Eq[Exception] = Eq.fromUniversalEquals
//
//
//
//  case class Portfolio(p:String)
//  case class PortfolioE(portfolio:Portfolio)
//  case class Node(node:String)
//  case class NodeE(node:Node)
//  case class Result(r:String)
//  case class Response(result:Result)
//  type ExOr[R] = Ior[Exception,R]
//  type Ex = Exception
//  val ntoEntity: (Node, Portfolio) => Right[Ex, NodeE] = (n:Node, p:Portfolio) =>Right[Ex,NodeE](NodeE(n))
//  val ptoEntity: Portfolio => Either[Ex, PortfolioE] = (p:Portfolio) => Ior.right[Ex, PortfolioE](PortfolioE(p)).toEither
//  val compute: (NodeE, PortfolioE) => Either[Ex, Result] = (ne:NodeE, pe:PortfolioE) => Ior.right[Ex,Result](Result("result")).toEither
//  val renderResponse: Result => Either[Ex, Response] = (r:Result) => Ior.right[Ex,Response](Response(r)).toEither
//  val portfolio = Portfolio("porftolio")
//  val node = Node("node")
//  for{
//
//    nodeE <- ntoEntity(node, portfolio)
//    portfolioE <- ptoEntity(portfolio)
//    result <- compute(nodeE,portfolioE)
//    response <- renderResponse(result)
//
//
//  } yield response
//
//
//  import cats.arrow.Arrow
//  import cats.data.Kleisli
//  import cats.implicits._
//
//  def combine[F[_, _]: Arrow, A, B, C](fab: F[A, B], fac: F[A, C]): F[A, (B, C)] =
//    Arrow[F].lift((a: A) => (a, a)) >>> (fab *** fac)
//  case class Request(n:Node, p:Portfolio)
//  val r2Node:Request => Right[Ex, NodeE] = (r:Request) =>Right[Ex,NodeE](NodeE(r.n))
//  val r2Portfolio: Request => Right[Ex,PortfolioE] = (r:Request) => Right[Ex,PortfolioE](PortfolioE(r.p))
//
//  val r2NodeK = Kleisli(r2Node)
//  val r2PortfolioK = Kleisli(r2Portfolio)
//
//  val combined = combine(r2NodeK,r2PortfolioK)
//
//  val addEmpty: Int => Int = _ + 0
//  val multiplyEmpty: Int => Double= _ * 1d
//  val f: Int => (Int, Double) = addEmpty &&& multiplyEmpty
//  val k = Kleisli(ptoEntity)
//  val s = Kleisli.liftF(ntoEntity)
//  val id = Kleisli.liftF((n:Node,p:Portfolio)=> Ior.right[Ex,(Node,Portfolio)](n,p))
// //Arrow[Function2].split(ntoEntity, Arrow[Function1].second(ptoEntity))
//
//
//
//  import cats.arrow.Arrow
//  import cats.data.Kleisli
//  import cats.implicits._
//
//  def combine[F[_, _]: Arrow, A, B, C](fab: F[A, B], fac: F[A, C]): F[A, (B, C)] =
//    Arrow[F].lift((a: A) => (a, a)) >>> (fab *** fac)
//
//  val headK: Kleisli[Option, List[Int], Int] = Kleisli((_: List[Int]).headOption)
//  val lastK = Kleisli((_: List[Int]).lastOption)
//  val headPlusLast = combine(headK, lastK).andThen(Arrow[Kleisli[Option, ?, ?]].lift(((_: Int) + (_: Int)).tupled))
// // val headPlusLast = combine(headK, lastK).>>>(Arrow[Kleisli[Option, ?, ?]].lift(((_: Int) + (_: Int)).tupled))
//
//val fun: Kleisli[Option, List[Int], (Int, Int)] = combine(headK, lastK)
//
//
//}
