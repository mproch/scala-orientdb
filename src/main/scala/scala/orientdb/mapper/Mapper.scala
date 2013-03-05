package scala.orientdb.mapper

import orientdb.document.ODocumentS

trait Mapper {

  def map[T<:AnyRef:Manifest](doc:ODocumentS) : Either[List[String], T]

  def unmap[T<:AnyRef](doc:ODocumentS, obj:T) : ODocumentS

}
