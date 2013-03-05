package scala.orientdb.types

case class Entry[T](converter:OrientDBType[T],value:T) {
  def convertToDatabase = converter.convertToDatabase(value)

  def asType = new OrientDBType[Entry[T]] {
    def convert(value: Object) = converter.convert(value).map(Entry(converter,_))

    def convertToDatabase(value: Entry[T]) = converter.convertToDatabase(value.value)
  }

}


object Entry {
  def apply[T:OrientDBType](value:T) : Entry[T] = Entry(implicitly[OrientDBType[T]], value)
}

