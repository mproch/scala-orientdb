package scala.orientdb.connection

case class ConnectionData(connectionType:ConnectionType.ConnectionType, url:String,login:String,password:String) {
  def connectionString = connectionType.toString+":"+url
}

object ConnectionType extends Enumeration {
  type ConnectionType = Value
  val memory, local, remote = Value

}