package jdc.samples

import orientdb.connection.{Connection, ConnectionType, ConnectionData}
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx
import orientdb.types.EmptyHMap
import scala.orientdb.types.OrientDBTypes._
import collection.JavaConversions
import JavaConversions._
import orientdb.document.ODocumentS

object LoadServer extends App {

  //val server = new OServer
  //server.startup(getClass.getResourceAsStream("dbConfig.xml"))

  val connectionData = ConnectionData(ConnectionType.memory, "test", "admin", "admin")

  val connection = Connection(new ODatabaseDocumentTx(connectionData.connectionString).create())


  List("Warsaw","London","Cairo","New York","Port Said","Cracow","Giza")
    .map(prepareBranch).foreach(_.run(connection))

  println(connection.underlyingConnection.browseClass("branch").iterator().toList)

  def prepareSaleAgents() {

  }
  prepareSaleAgents()

  def prepareCustomers() {

  }
  prepareCustomers()



  while (true) {

    Thread.sleep(10000)
    println("still running")

  }

  private def prepareBranch(code:String) =
    ODocumentS("branch",
      EmptyHMap ~ ("code"->code) ~ ("name"->s"Branch $code")).save().map(_.id)

}
