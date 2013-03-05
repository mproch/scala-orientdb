package scala.orientdb.utils

trait Monad[M[_]] {

  def fmap[A,B](x:M[A], f:A=>M[B]) : M[B]
  def point[A](x:A) : M[A]

  def map[A,B](x:M[A], f:A=>B) : M[B] = fmap(x, f andThen point)

}

object OptionMonad extends Monad[Option] {

  def fmap[A, B](x: Option[A], f: (A) => Option[B]) = x.flatMap(f)

  def point[A](x: A) = Some(x)

}

object ListMonad extends Monad[List] {

  def fmap[A, B](x: List[A], f: (A) => List[B]) = x.flatMap(f)

  def point[A](x: A) = List(x)
}

object IterableMonad extends Monad[Iterable] {

  def fmap[A, B](x: Iterable[A], f: (A) => Iterable[B]) = x.flatMap(f)

  def point[A](x: A) = List(x) : Iterable[A]

}

object Monad {

  implicit val list = ListMonad
  implicit val option = OptionMonad
  implicit val iterable = IterableMonad

}