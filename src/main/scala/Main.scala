import simulacrum._

object ter extends App {


  @typeclass trait Mysemigroup[A] {
    @op("|++|") def append(x: A, y: A): A
  }
  object IntMysemigroup {

    implicit val mysemigroupInt: Mysemigroup[Int] = new Mysemigroup[Int] {
      def append(x: Int, y: Int) = x + y
    }
  }
  import IntMysemigroup.mysemigroupInt
  import Mysemigroup.ops._

  println(3 |++| 4)

}
