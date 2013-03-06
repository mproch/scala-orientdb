package scala.orientdb.utils

trait NewType[T] {

  val value:T

}


object NewType {
  implicit def unwrap[T](newType:NewType[T]) : T = newType.value
}
