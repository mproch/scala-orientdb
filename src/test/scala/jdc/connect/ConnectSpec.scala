package jdc.connect

import scala.orientdb.connection.{ConnectionType, ConnectionData}
import scala.orientdb.connection.template.WithDocumentConnectionPooled
import orientdb.connection.monadic.{WithConnectionT, DocumentHelper}
import orientdb.types.OrientDBTypes
import orientdb.utils.Monad

object ConnectSpec extends App with WithDocumentConnectionPooled {

  val connectionData = ConnectionData(ConnectionType.memory, "jdc1", "admin", "admin")

  doWithConnection[Unit](_.executeCommand("select from database"))

}

object MonadicConnectSpec extends App {

  import OrientDBTypes._
  import WithConnectionT._
  import Monad._

  val a = for {
    loaded <- point(DocumentHelper.load("a"))
    data <- pointT(loaded.field[String]("name"))
    loaded2 <- DocumentHelper.load(data)
  } yield (loaded2)

  val b = for {
    s <- point(DocumentHelper.query("select from "))
    id <- pointT(s.field[String]("z").toIterable)
    c <- DocumentHelper.query("select from where ",id)
  } yield (c)

}
