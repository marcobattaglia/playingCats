package com.company

import cats.data.Ior
import cats.kernel.{Eq, Semigroup}
sealed trait ExceptionCombinator
object ExceptionCombinator{
  implicit val exceptionCombinator: Semigroup[Exception] =new Semigroup[Exception]{
    override def combine(x: Exception, y: Exception) = x
  }
  implicit def eqTree: Eq[Exception] = Eq.fromUniversalEquals



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
  import cats.data.Kleisli
  import cats.instances.all._
  import cats.syntax.all._
  import cats.instances.EitherInstances

  val addEmpty: Int => Int = _ + 0
  val multiplyEmpty: Int => Double= _ * 1d
  val f: Int => (Int, Double) = addEmpty &&& multiplyEmpty
  val k = Kleisli(ptoEntity)
  val s = Kleisli.liftF(ntoEntity)
  val id = Kleisli.liftF((n:Node,p:Portfolio)=> Ior.right[Ex,(Node,Portfolio)](n,p))
 //Arrow[Function2].split(ntoEntity, Arrow[Function1].second(ptoEntity))




}
