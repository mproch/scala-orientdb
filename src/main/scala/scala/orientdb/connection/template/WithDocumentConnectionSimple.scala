package scala.orientdb.connection.template

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx
import orientdb.connection.ConnectionData

trait WithDocumentConnectionSimple extends WithDocumentConnection {
  def acquireConnection = new ODatabaseDocumentTx(connectionData.connectionString)
    .open(connectionData.login, connectionData.password)
}


object WithDocumentConnectionSimple {

  def apply(connectionDataP:ConnectionData) = new WithDocumentConnectionSimple {
    def connectionData = connectionDataP
  }

}