package scala.orientdb.document

import com.orientechnologies.orient.core.record.impl.ODocument
import orientdb.types._
import orientdb.connection.monadic.WithConnection
import orientdb.connection.Connection
import orientdb.types.OId

object ODocumentS {
  implicit def toODocumentS(odoc:ODocument) = new ODocumentS(odoc)

  def apply(clazz:String) : ODocumentS = ODocumentS(new ODocument(clazz))

  def apply(clazz:String, value:HMap) : ODocumentS = ODocumentS(clazz) mod value
}

case class ODocumentS(underlying:ODocument) extends  MapTraversable {

  def field[T:OrientDBType](name:String) =
    rawField(name).flatMap(implicitly[OrientDBType[T]].convert)

  def id = OId(underlying.getIdentity.getClusterId, underlying.getIdentity.getClusterPosition.longValue())

  def rawField(name:String) : Option[Object] = Option(underlying.field[Object](name))

  def @@[T: OrientDBType] : Option[T]= None

  protected def nonEmptyModifier(field: String) =
    new Modifier {
      def rawMod(newVal: Object) = ODocumentS(underlying.field(field,newVal))
    }
  def save() = WithConnection((c:Connection) => ODocumentS(c.save(this)))

  def mod(value:HMap) : ODocumentS = value match {
    case EmptyHMap => this
    case FilledHMap(headName, head,tail) =>  {
      implicit val typ = head.asType
      this mod tail nonEmptyModifier headName mod head
    }
  }

}