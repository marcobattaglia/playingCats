import scala.concurrent.ExecutionContext
import scala.concurrent.Future

object EvenHarderExample {
  import Types._

  trait FooRepo {
    def foo()(implicit ec: ExecutionContext): Future[Error Or Foo]
  }

  trait BarRepo {
    def bar(): Error Or Bar
  }

  trait BazRepo {
    def baz(): Baz
  }

  class Controller(fooRepo: FooRepo, barRepo: BarRepo, bazRepo: BazRepo) {
    import cats.instances.future._
    def foobarbaz()(implicit ec: ExecutionContext): Future[Error Or (Foo, Bar, Baz)] = {
      OrT.value {
        for {
          foo <- OrT(fooRepo.foo())
          bar <- OrT.fromOr(barRepo.bar())
          baz <- OrT.pure(bazRepo.baz())
        } yield {
          (foo, bar, baz)
        }
      }
    }
  }
}