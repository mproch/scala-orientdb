package scala.orientdb.types

import java.util.{Map=>JMap}
import java.util.{List=>JList}

import scala.collection.JavaConversions._
import collection.JavaConverters
import JavaConverters._
import java.util
import com.orientechnologies.orient.core.id.{ORID, OClusterPositionFactory, ORecordId}
import com.orientechnologies.orient.core.record.impl.ODocument


trait OrientDBType[T] {
  def convert(value:Object) : Option[T]

  def convertToDatabase(value:T) : Object

}

object StringType extends OrientDBType[String] {
  def convert(value: Object) = Option(value).map(_.toString)

  def convertToDatabase(value: String) = value
}

object MapType extends OrientDBType[Map[String,Object]] {
  def convert(value: Object) = value match {
    case e:Map[String,Object] => Some(e)
    case e:JMap[String,Object] => Some(e.toMap)
    case _ => None
  }

  def convertToDatabase(value: Map[String, Object]) = new util.HashMap[String,Object](value.asJava)
}

object AnyMapType extends OrientDBType[Map[String,Any]] {
  def convert(value: Object) = value match {
    case e:Map[String,Any] => Some(e)
    case e:JMap[String,Any] => Some(e.toMap)
    case _ => None
  }

  def convertToDatabase(value: Map[String,Any]) = new util.HashMap[String,Any](value.asJava)
}


object ListType extends OrientDBType[List[Object]] {
  def convert(value: Object) = value match {
    case e:List[Object] => Some(e)
    case e:JList[Object] => Some(e.toList)
    case _ => None
  }

  def convertToDatabase(value: List[Object]) = new util.ArrayList[Object](value.asJava)
}

object AnyListType extends OrientDBType[List[Any]] {
  def convert(value: Object) = value match {
    case e:List[Any] => Some(e)
    case e:JList[Any] => Some(e.toList)
    case _ => None
  }

  def convertToDatabase(value: List[Any]) = new util.ArrayList[Any](value.asJava)
}


object OIdType extends OrientDBType[OId] {
  def convert(value: Object) = value match {
    case e:ORecordId => toOID(e)
    case e:ODocument => toOID(e.getIdentity)
    case _ => None
  }

  private def toOID(e:ORID) =
    if (e.isInstanceOf[ORecordId]) Some(OId(e.getClusterId, e.getClusterPosition.longValue())) else None


  def convertToDatabase(value: OId) =
    new ORecordId(value.cluster, OClusterPositionFactory.INSTANCE.valueOf(value.position))
}


object OrientDBTypes {

  implicit val StringTypeV = StringType
  implicit val ListTypeV = ListType
  implicit val AnyListTypeV = AnyListType

  implicit val MapTypeV = MapType
  implicit val AnyMapTypeV = AnyMapType

  implicit val OIdTypeV = OIdType

  implicit val HMapTypeV = HMapType
  implicit val HListTypeV = HListType

}




/*class StringType extends OrientDBType[String]
class LongType extends OrientDBType[Long]
class DoubleType extends OrientDBType[Double]
class FloatType extends OrientDBType[Float]
  */




