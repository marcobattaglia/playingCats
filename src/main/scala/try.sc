import scala.util.Try
import cats._
import cats.implicits._
def tryable(something:Int):Try[Int] = Try{
  3/(something-something+1)
}


val tryA: Try[(Int) => Int] = Try{ (x:Int) => 4/x}

val result: Try[Int] = Apply[Try].ap(tryA)(tryable(1))

val ff=  tryable(1) |@| tryA

Option(1) |@| Option(2)

def innerTry(x:Int): Try[Try[Int]] = Try{
  tryable(x)
}

val c = innerTry(3)
c.flatten.toOption
