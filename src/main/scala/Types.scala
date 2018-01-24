import scala.concurrent.Future

import cats.Applicative
import cats.data.EitherT
import cats.data.OptionT

object Types {
  final case class Error(msg: String)
  final case class Foo(value: String)
  final case class Bar(value: String)
  final case class Baz(value: String)

  type Or[+A, +B]      = Either[A, B]
  type OrT[F[_], A, B] = EitherT[F, A, B]

  type MyEitherT[B] = EitherT[Future, Error, B]
  type MyOptionT[B] = OptionT[MyEitherT, B]

  object OrT {
    final def apply[F[_], A, B](value: F[A Or B])                = EitherT(value)
    final def value[F[_], A, B](stack: EitherT[F, A, B])         = stack.value
    final def pure[F[_], A, B](b: B)(implicit F: Applicative[F]) = EitherT.pure[F, A, B](b)
    final def fromOr[F[_]]: FromOrPartiallyApplied[F]            = new FromOrPartiallyApplied
    final class FromOrPartiallyApplied[F[_]] private[OrT] {
      final def apply[E, A](or: E Or A)(implicit F: Applicative[F]): OrT[F, E, A] =
        OrT(F.pure(or))
    }
  }
}