package scala.orientdb

case class WithConnection[T](action:Connection=>T) {

  def map[H](function:T=>H) = copy(action = action andThen function)

  def flatMap[H](function:T=>WithConnection[H]) = (c:Connection) =>
     (action andThen function)(c).run(c)

  def run(connection:Connection) = action(connection)
}

object WithConnection {
  def apply[T](o:T) = WithConnection((c:Connection)=>o)
}
