package scala.orientdb.connection.template

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentPool
import orientdb.connection.ConnectionData

trait WithDocumentConnectionPooled extends WithDocumentConnection {

  def connectionPool = ODatabaseDocumentPool.global()

  def acquireConnection = connectionPool
    .acquire(connectionData.connectionString, connectionData.login, connectionData.password)
}

object WithDocumentConnectionPooled {

  def apply(connectionDataP:ConnectionData) = new WithDocumentConnectionPooled {
    def connectionData = connectionDataP
  }

}