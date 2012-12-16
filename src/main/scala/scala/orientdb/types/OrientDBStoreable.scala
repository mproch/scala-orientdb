package scala.orientdb.types

trait OrientDBType[T] {

}

class StringType extends OrientDBType[String]


class LongType extends OrientDBType[String]
class DoubleType extends OrientDBType[String]
class FloatType extends OrientDBType[String]


class ProductType2[T1:OrientDBType,T2:OrientDBType] extends OrientDBType[(T1,T2)]

