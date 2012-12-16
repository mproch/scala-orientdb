package scala.orientdb

import com.orientechnologies.orient.core.db.document.ODatabaseDocument

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

}
