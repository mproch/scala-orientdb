package scala.orientdb.connection.template

import org.scalatest.{BeforeAndAfterEach, BeforeAndAfterAll, FunSpec}
import org.scalatest.matchers.MustMatchers
import orientdb.connection.{ConnectionType, ConnectionData, Connection}
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx

class WithDocumentConnectionSpec extends MustMatchers with FunSpec with BeforeAndAfterEach {

  var openConnection: ODatabaseDocumentTx = null

  val connectionData = ConnectionData(ConnectionType.memory, "test", "admin", "admin")





  describe("Connections with templates") {

    it("performs operations with pooled connections") {

    }


    it("performs operations with simple connections") {

    }

  }

  override protected def beforeEach() {
    openConnection = new ODatabaseDocumentTx(connectionData.connectionString).create()
  }

  override protected def afterEach() {
    openConnection.close()
  }
}



