import cats.arrow.Arrow
import cats.data.Kleisli
import cats.instances.all._
import cats.syntax.all._
type ExEr[R] = Either[Exception,R]
val div = (x:Int) => if(x==0) Left(new Exception("divByZero")) else Right(5/x)
val divK= Kleisli(div)
val c= Arrow[Kleisli[ExEr,?,?]].merge(divK,divK)
val d = divK *** (divK)
val rt = d(0,2)
d(2,2)