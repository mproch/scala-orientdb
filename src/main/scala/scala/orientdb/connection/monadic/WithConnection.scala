package scala.orientdb.connection.monadic

import orientdb.connection.Connection
import orientdb.document.ODocumentS
import orientdb.connection.template.WithDocumentConnection
import orientdb.utils.Monad

object WithConnection {
  def point[T](a:T) = apply(a)

  def apply[T](o:T) : WithConnection[T] = WithConnection((c:Connection)=>o)
}

case class WithConnection[T](action:Connection=>T) {

  def map[H](function:T=>H) = copy(action = action andThen function)

  def flatMap[H](function:T=>WithConnection[H]) = WithConnection((c:Connection) =>
     (action andThen function)(c).run(c))

  def run(connection:Connection) : T = action(connection)

  def run(connectionData:WithDocumentConnection) : T = connectionData.doWithConnection[T](run)

  def point[M[_]:Monad] : WithConnectionT[M,T] = WithConnectionT(map(implicitly[Monad[M]].point[T]))

}

object WithConnectionT {

  implicit def point[M[_]:Monad,T](action:WithConnection[M[T]]) : WithConnectionT[M,T] = WithConnectionT(action)

  implicit def pointT[M[_]:Monad, T](opt:M[T]) : WithConnectionT[M,T] = WithConnectionT(WithConnection.point(opt))
}

case class WithConnectionT[M[_]:Monad,T](action:WithConnection[M[T]]) {

  def run(conn:Connection) = action.run(conn)

  val monad = implicitly[Monad[M]]

  def map[H](function:T=>H) = copy(action = action.map(monad.map(_,function)))

  def flatMap[H](function:T=>WithConnectionT[M,H]) : WithConnectionT[M,H] = WithConnectionT[M,H](WithConnection[M[H]](
    (conn:Connection) => {
      val ev1 = run(conn)
      val f2  = (c:T) => function(c).run(conn)
      monad.fmap(ev1, f2)
    }
  ))
}

object DocumentHelper {

  def newInstance(klass:String) = WithConnection[ODocumentS]((c:Connection)=> c.newInstance(klass))

  def load(id:String) = WithConnection((c:Connection) => c.load(id))

  def query(query:String, args:Object*) =
    WithConnection((c:Connection)=>c.query(query,args :_*))

}