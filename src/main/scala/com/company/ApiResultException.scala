package com.company

import cats.kernel.{Eq, Semigroup}
sealed trait ExceptionCombinator
object ExceptionCombinator{
  implicit val exceptionCombinator: Semigroup[Exception] =new Semigroup[Exception]{
    override def combine(x: Exception, y: Exception) = x
  }
  implicit def eqTree: Eq[Exception] = Eq.fromUniversalEquals
}
