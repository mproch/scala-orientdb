package scala.orientdb

import com.orientechnologies.orient.core.db.document.ODatabaseDocument
import com.orientechnologies.orient.core.record.impl.ODocument
import com.orientechnologies.orient.core.command.OCommandRequest
import com.orientechnologies.orient.core.sql.OCommandSQL
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery
import com.orientechnologies.orient.core.id.ORecordId
import scala.collection.JavaConversions._

case class Connection(underlyingConnection: ODatabaseDocument) {


  def executeCommand(command: String, args: Object*) {
    underlyingConnection.command[OCommandRequest](new OCommandSQL(command)).execute(args: _*)
  }

  def query(query: String, args: Object*): Iterable[ODocument] =
    underlyingConnection.query[java.util.List[ODocument]](new OSQLSynchQuery[ODocument](query), args: _*)

  def load(id: String, klazz: String) =
    query("select from " + klazz + " where id = ? ", id).headOption

  def load(id: String) = Option(underlyingConnection.load[ODocument](new ORecordId(id)))

  def delete(id: String, klazz: String) {
    executeCommand("delete from " + klazz + " where id = ? ", id)
  }

  def save(doc: ODocument) = underlyingConnection.save[ODocument](doc)

  def newInstance(className: String) = underlyingConnection.newInstance[ODocument](className)

  def newInstance() = underlyingConnection.newInstance[ODocument]()

}
