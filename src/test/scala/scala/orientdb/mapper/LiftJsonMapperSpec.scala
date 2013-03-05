package scala.orientdb.mapper

import liftJson.LiftJsonMapper
import orientdb.WithTestDatabase
import orientdb.connection.monadic.DocumentHelper
import org.scalatest.matchers.MustMatchers
import orientdb.types.OrientDBTypes

class LiftJsonMapperSpec extends MustMatchers with WithTestDatabase {

  describe("LiftJson serialization") {

    it("Saves mapped object") { conn =>

      implicit val mapper = new LiftJsonMapper()

      val agent = SaleAgent("123","Local")

      DocumentHelper.newInstance("saleAgent").flatMap(mapper.unmap(_,agent).save()).run(conn)

      val branch = DocumentHelper.query("select from saleAgent where code = '123'")
        .map(_.headOption.map(mapper.map[SaleAgent]))
        .run(conn)

      branch must be === Some(Right(agent))
    }

  }

}

case class SaleAgent(code:String, branch:String)