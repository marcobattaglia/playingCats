package com.company
import cats.kernel.laws.discipline.SemigroupTests
import com.company.ExceptionCombinator._



import cats.tests.CatsSuite

class IntCombinatorTest extends CatsSuite{

  import org.scalacheck.{Arbitrary, Gen}

  implicit val arbInt = Arbitrary.arbInt
  implicit val arbException = Arbitrary.arbException

  checkAll("Int.SemigroupLaws", SemigroupTests[Int].semigroup)
  checkAll("Exception.SemigroupLaws", SemigroupTests[Exception].semigroup)
}
