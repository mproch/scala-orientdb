package scala.orientdb

import connection.Connection
import connection.monadic.WithConnection
import org.scalatest.fixture
import com.orientechnologies.orient.core.db.document.{ODatabaseDocumentTx, ODatabaseDocument}
import org.scalatest.matchers.MustMatchers


trait WithTestDatabase extends MustMatchers with fixture.FunSpec {

  type FixtureParam = (Connection)

  override protected def withFixture(test: OneArgTest) {
    val conn = Connection(new ODatabaseDocumentTx("memory:"+test.name).create[ODatabaseDocument]())
    WithConnection(WithTestDatabase.init _).run(conn)
    test(conn)
    conn.close()
  }

}


object WithTestDatabase {

  private def readSQL(location:String) =
    scala.io.Source.fromInputStream(
      getClass.getResourceAsStream(location)
    ).mkString.split(";")


  def init(doc:Connection) {
    readSQL("/testDB/create.sql").map(_.trim).filter(_ != "").map(doc.executeCommand(_))
  }

}