package scala.orientdb.types

import java.util

sealed trait HMap {
  def ~[K:OrientDBType](entry:(String,K)) = FilledHMap(entry._1, Entry(entry._2),this)
}

object HMap {

  implicit def singleton[K:OrientDBType](entry:(String,K)) : HMap = EmptyHMap ~ entry

}

case object EmptyHMap extends HMap

case class FilledHMap[T](headName:String, head:Entry[T],tail:HMap) extends HMap

object HMapType extends OrientDBType[HMap] {

  //TODO:??
  def convert(value: Object) = None

  def convertToDatabase(value: HMap) : java.util.Map[String,Any] = value match {
    case EmptyHMap => new util.HashMap[String,Any]()
    case FilledHMap(headName, head,tail) => {
      val map = convertToDatabase(tail)
      map.put(headName,head.convertToDatabase)
      map
    }
  }
}