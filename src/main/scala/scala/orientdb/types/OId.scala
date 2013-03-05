package scala.orientdb.types

import orientdb.connection.Connection
import com.orientechnologies.orient.core.id.{OClusterPositionLong, ORecordId}
import orientdb.connection.monadic.WithConnection
import orientdb.document.ODocumentS

case class OId(cluster:Int, position:Long) {

  def toRecordId = new ORecordId(cluster, new OClusterPositionLong(position))

  def get : WithConnection[Option[ODocumentS]] = WithConnection(this.getWithConnection _)

  def getWithConnection(connection:Connection) = connection.load(this)
}
