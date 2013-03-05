package scala.orientdb.document

import orientdb.types.{ListType, MapType, OrientDBType}
import scala.orientdb.types.OrientDBTypes._


object ODocumentPart {
  def convertValue(mod:Modifier)(value:Object) : Modifiable = MapType
    .convert(value).map(OEmbedded(mod,_)).getOrElse(OValue(mod, value))

}


sealed trait Modifiable extends Traversable{

  def mod[T:OrientDBType](newVal:T) = modifier.mod(newVal)

  def modifier:Modifier

}


object Empty {
  def apply(name:String, modifier:Modifier) : Empty = Empty(new Modifier {
    def rawMod(newVal: Object) = modifier.mod(Map[String,Any](name->newVal))
  })
}

case class Empty(modifier:Modifier) extends Modifiable {

  def \(fieldName: String) = Empty(fieldName,modifier)

  def \\(fieldName: String) = OList(modifier, List())

  def @@[T: OrientDBType] = None

}

case class OEmbedded(modifier:Modifier,
                     partial:Map[String,Object]) extends Modifiable with MapTraversable {

  protected def rawField(name: String) = partial.get(name)

  protected def nonEmptyModifier(field: String) = new Modifier {
    def rawMod(newVal: Object) = modifier.mod(partial.updated(field, newVal))
  }

  def @@[T: OrientDBType] = implicitly[OrientDBType[T]].convert(partial)

}

case class OValue(modifier:Modifier, obj:Object) extends Modifiable {

  def \(fieldName: String) = Empty(fieldName,modifier)

  def \\(fieldName: String) = OList(modifier, List())

  def @@[T: OrientDBType] = implicitly[OrientDBType[T]].convert(obj)

}

case class OList(modifier:Modifier, value:List[Object]) {

  private def listModifier(index:Int) = new Modifier {
    def rawMod(newVal: Object) = modifier.mod(update(index,newVal))
  }

  private def update(index:Int, newVal:Any) =
      value.padTo(index+1,null).updated(index, newVal)

  def get(index:Int) = apply(index)

  def apply(index:Int) : Modifiable =
    if (value.size > index)
      ODocumentPart.convertValue(listModifier(index))(value(index)) else Empty(listModifier(index))

}

trait Modifier {

  def rawMod(obj:Object) : ODocumentS

  def mod[T: OrientDBType](newVal: T) = rawMod(implicitly[OrientDBType[T]].convertToDatabase(newVal))
}

trait Traversable {

  def \(fieldName:String) : Modifiable

  def \\(fieldName:String) : OList

  def @@[T:OrientDBType] : Option[T]

  def @@[T:OrientDBType](classOf:Class[T]) : Option[T] = @@[T]

}

trait MapTraversable extends Traversable {

  def \(fieldName: String) =
    rawField(fieldName) map ODocumentPart.convertValue(nonEmptyModifier(fieldName)) getOrElse Empty(nonEmptyModifier(fieldName))

  def \\(fieldName: String) = convertList(nonEmptyModifier(fieldName),rawField(fieldName))

  protected def rawField(name:String) : Option[Object]

  protected def nonEmptyModifier(field:String) : Modifier

  private def convertList(mod:Modifier, value:Option[Object]) = OList(mod,
    value.flatMap(ListType.convert).getOrElse(List[Object]()))

}


