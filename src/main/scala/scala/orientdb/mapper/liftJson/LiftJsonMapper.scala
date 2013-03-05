package scala.orientdb.mapper.liftJson

import orientdb.mapper.Mapper
import orientdb.document.ODocumentS
import net.liftweb.json._
import net.liftweb.json.Serialization.{read, write}
import com.orientechnologies.orient.core.record.impl.ODocument

class LiftJsonMapper(formats : Formats = Serialization.formats(NoTypeHints)) extends Mapper {

  implicit val formatsImpl = formats

  def map[T<:AnyRef:Manifest](doc: ODocumentS) = Right(read[T](doc.underlying.toJSON))

  def unmap[T<:AnyRef](doc: ODocumentS, obj: T) = {
    val serialized = write[T](obj)
    ODocumentS(doc.underlying.fromJSON[ODocument](serialized))
  }
}
