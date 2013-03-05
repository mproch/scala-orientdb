package scala.orientdb.connection.template

import com.orientechnologies.orient.core.db.document.ODatabaseDocument
import orientdb.connection.{ConnectionData, Connection}

trait WithDocumentConnection {

  def connectionData : ConnectionData

  def acquireConnection : ODatabaseDocument

  def doWithConnection[T](action:Connection=>T) : T = {
    val oconnection = acquireConnection
    try {
      action(Connection(oconnection))
    } finally {
      if (!oconnection.isClosed) oconnection.close()
    }
  }

  def apply[T](action:Connection=>T) = doWithConnection(action)

}
