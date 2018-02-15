import cats.arrow.Arrow
import cats.data.Ior.{Left => l, Right => r}
import cats.data.{Ior, Kleisli}
import cats.instances.all._
import cats.syntax.all._


case class Request(n:Node, p:Portfolio)

case class Portfolio(p:String)
case class PortfolioE(portfolio:Portfolio)
case class Node(node:String)
case class NodeE(node:Node)
case class Result(r:String)
case class Response(result:Result)
type Ex = Exception
type ExOr[R] = Ior[Ex,R]
type ExEr[R] = Either[Exception,R]

val ntoEntity: Request => ExOr[NodeE] = (r:Request) => Ior.right[Ex, NodeE](NodeE(r.n))
val pRequest = (r:Request)=> r.p
//val ptoEntity: (Portfolio) => ExOr[PortfolioE] = (p:Portfolio) => Ior.right[Ex, PortfolioE](PortfolioE(p))

val ptoEntity2: Portfolio => ExOr[PortfolioE] = (p:Portfolio) => Ior.right[Ex, PortfolioE](PortfolioE(p))
val ptoEntity: Request => ExOr[PortfolioE] = pRequest.andThen(ptoEntity2)
val compute: (NodeE, PortfolioE) => ExOr[Result] = (ne:NodeE, pe:PortfolioE) => Ior.right[Ex,Result](Result("result"))
val renderResponse: Result => ExOr[Response] = (r:Result) => Ior.right[Ex,Response](Response(r))
val portfolio = Portfolio("porftolio")
val node = Node("node")



val pint= (x:Int)=> if(x<10) Right(x) else Left(new Exception(""))

val k = Kleisli(ntoEntity)
val k2 = Kleisli(pint)
val k3 = Kleisli.liftF(pint)
val k4 = Kleisli(ptoEntity)
val merged = (ntoEntity).merge(ptoEntity)

def combine[F[_, _]: Arrow, A, B, C](fab: F[A, B], fac: F[A, C]): F[A, (B, C)] =
  Arrow[F].lift((a: A) => (a, a)) >>> (fab *** fac)

val headK:Kleisli[Option,List[Int],Int] = Kleisli((_: List[Int]).headOption)
val lastK:Kleisli[Option,List[Int],Int] = Kleisli((_: List[Int]).lastOption)
val combinedInt:Kleisli[Option,List[Int],(Int, Int)] = combine(headK,lastK)
val headPlusLast = combine(headK, lastK) andThen Arrow[Kleisli[Option, ?, ?]].lift(((_: Int) + (_: Int)).tupled)


val r2Node:Request => ExOr[NodeE] = (r:Request) =>Ior.right[Ex, NodeE](NodeE(r.n))
val r2Portfolio: Request => ExOr[PortfolioE] = (r:Request) => Ior.right[Ex, PortfolioE](PortfolioE(r.p))

val r2NodeK:Kleisli[ExOr,Request,NodeE] = Kleisli(r2Node)
val r2PortfolioK:Kleisli[ExOr,Request,PortfolioE] = Kleisli(r2Portfolio)
//val combo: Kleisli[ExOr,Request,(NodeE,PortfolioE)] = combine(r2NodeK,r2PortfolioK)
import cats.Monad

//
//implicit def eitherMonad[Err]: Monad[Either[Err, ?]] =
//  new Monad[Either[Err, ?]] {
//    def flatMap[A, B](fa: Either[Err, A])(f: A => Either[Err, B]): Either[Err, B] =
//      fa.flatMap(f)
//
//    def pure[A](x: A): Either[Err, A] = Either.right(x)
//
//    @annotation.tailrec
//    def tailRecM[A, B](a: A)(f: A => Either[Err, Either[A, B]]): Either[Err, B] =
//      f(a) match {
//        case Right(Right(b)) => Either.right(b)
//        case Right(Left(a)) => tailRecM(a)(f)
//        case l@Left(_) => l.rightCast[B] // Cast the right type parameter to avoid allocation
//      }
//  }

val r2NodeEi:Request => ExEr[NodeE] = (r:Request) =>Ior.right[Ex, NodeE](NodeE(r.n)).toEither
val r2PortfolioEi: Request => ExEr[PortfolioE] = (r:Request) => Ior.right[Ex, PortfolioE](PortfolioE(r.p)).toEither



val r2NodeKEi: Kleisli[ExEr, Request, NodeE] = Kleisli(r2NodeEi)
implicit val r2PortfolioKEi:Kleisli[ExEr,Request,PortfolioE] = Kleisli(r2PortfolioEi)
val result: Request => (ExEr[NodeE], ExEr[PortfolioE]) = combine(r2NodeEi,r2PortfolioEi)
val resultCombined = combine(r2PortfolioKEi ,r2NodeKEi)
val resultK =  Arrow[Kleisli[ExEr,?,?]].merge(r2PortfolioKEi ,r2NodeKEi)
val req = Request(Node("node"),Portfolio("portfolio"))
resultK(req)
import cats.implicits._
class arrOps[A,B,C](implicit k:Kleisli[ExEr,A,B]){
  def mer(g:Kleisli[ExEr,A,C]) =Arrow[Kleisli[ExEr,?,?]].merge(k,g)
}

