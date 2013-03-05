package scala.orientdb.types

import java.util

trait HList

case object EmptyHList extends HList

case class FilledHList[T:OrientDBType](entry:Entry[T],tail:HList) extends HList{

  def ::[K:OrientDBType](value:K) = FilledHList(Entry(value),this)
}

object HListType extends OrientDBType[HList] {
  //TODO!!!
  def convert(value: Object) = None

  def convertToDatabase(value: HList) : java.util.List[Any] = value match {
    case EmptyHList => new util.ArrayList[Any]
    case FilledHList(head,tail) => {
      val list = convertToDatabase(tail)
      list.add(0, head.convertToDatabase)
      list
    }
  }

}