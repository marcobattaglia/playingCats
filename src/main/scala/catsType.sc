trait Printable[A]{
  def format(value:A):String

  def contramap[B](f:B => A): Printable[B] = {
    val self = this
    new Printable[B]{
      def format(value:B) = self.format(f(value))
    }
  }
}
object PrintableInstances{
  implicit val intPrintable= new Printable[Int]{
    def format(value:Int) = value.toString
  }
  implicit val stringPrintable = new Printable[String]{
    def format(value:String) = "v:" + value
  }
}
object PrintableSyntax{
  implicit class PrintableOps[A](value:A){
    def format(implicit p:Printable[A]):String = {
      p.format(value)
    }
    def print(implicit p:Printable[A]):Unit = {
      println(p.format(value))
    }

  }
}

object Printable {
  def format[A](input: A)(implicit p: Printable[A]): String =
    p.format(input)
  def print[A](input: A)(implicit p: Printable[A]): Unit =
    println(format(input))
}


final case class Cat(
                      name: String,
                      age: Int,
                      color: String
                    )
import PrintableSyntax._
import PrintableInstances._
import cats.syntax.SemigroupalBuilder

import scala.util.Try



//import scala.collection.immutable.{StringLike => _, _}

implicit val catPrintable = new Printable[Cat] {
  def format(cat: Cat) = {
    val name = Printable.format(cat.name)
    val age = cat.age.format
    val color = Printable.format(cat.color)
    s"$name is a $age year-old $color cat."
  }
}
val cat = Cat("name", 2,"black")
cat.print


implicit val booleanPrintable =
  new Printable[Boolean] {
    def format(value: Boolean): String =
      if(value) "yes" else "no"

  }


true.format

final case class Box[A](value:A)

implicit def boxPrintable[A](implicit p: Printable[A]) =
  p.contramap[Box[A]](_.value)

Box(true).format


trait Codec[A] {
  def encode(value: A): String
  def decode(value: String): Option[A]
  def imap[B](dec: A => B, enc: B => A): Codec[B] = {
    val self = this
    new Codec[B] {
      override def encode(value: B): String = self.encode(enc(value))

      override def decode(value: String):Option[B] = self.decode(value).map(dec)
    }
  }
}
def encode[A](value: A)(implicit c: Codec[A]): String =
  c.encode(value)
def decode[A](value: String)(implicit c: Codec[A]): Option[A] =
  c.decode(value)

implicit def boxCodec[A](implicit c: Codec[A]): Codec[Box[A]] =
  c.imap[Box[A]](Box(_), _.value)
