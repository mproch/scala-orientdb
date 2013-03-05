package scala.orientdb.connection

import com.orientechnologies.orient.core.db.document.ODatabaseDocument
import com.orientechnologies.orient.core.record.impl.ODocument
import com.orientechnologies.orient.core.command.OCommandRequest
import com.orientechnologies.orient.core.sql.OCommandSQL
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery
import com.orientechnologies.orient.core.id.ORecordId
import scala.collection.JavaConversions._
import orientdb.document.ODocumentS
import orientdb.types.OId
import orientdb.mapper.Mapper


case class Connection(underlyingConnection: ODatabaseDocument) {

  def close() {
    underlyingConnection.close()
  }

  def executeCommand(command: String, args: Object*) {
    underlyingConnection.command[OCommandRequest](new OCommandSQL(command)).execute(args: _*)
  }

  def query(query: String, args: Object*): Iterable[ODocumentS] =
    underlyingConnection.query[java.util.List[ODocument]](new OSQLSynchQuery[ODocument](query), args: _*)
    .map(ODocumentS.apply)

  def load(id: String, klazz: String) =
    query("select from " + klazz + " where id = ? ", id).headOption

  def load(id:OId) = Option(underlyingConnection.load[ODocument](id.toRecordId)).map(ODocumentS.apply)

  def delete(id:OId) = underlyingConnection.delete(id.toRecordId)

  def load(id: String) = Option(underlyingConnection.load[ODocument](new ORecordId(id))).map(ODocumentS.apply)

  def delete(id: String, klazz: String) {
    executeCommand("delete from " + klazz + " where id = ? ", id)
  }

  def save(doc: ODocumentS) = underlyingConnection.save[ODocument](doc.underlying)

  def newInstance(className: String) = ODocumentS(
    underlyingConnection.newInstance[ODocument](className))

  def newInstance() = ODocumentS(underlyingConnection.newInstance[ODocument]())

  def read[T<:AnyRef](id:OId)(implicit mapper:Mapper, manifest:Manifest[T]) = {
    load(id).map(mapper.map[T])
  }

  def write[T<:AnyRef](id:OId, obj:T)(implicit mapper:Mapper) = {
    val doc = load(id).getOrElse(newInstance())
    save(mapper.unmap(doc, obj))
  }

}
