package scala.orientdb.graph

import org.scalatest.matchers.MustMatchers
import orientdb.{OrientDriver, WithTestDatabase}
import OrientDriver._

class TraverseSpec extends MustMatchers with WithTestDatabase {

  describe("Basic traverse") {

    it("finds class data") { conn =>

      val names =for {
        traverse <- Traverse.forClass("customer")
          : WithConnectionI[Traverse]
        friend <- traverse.depth(3).field("friends").iterate
      } yield (friend \ "name" @@ classOf[String] getOrElse "noName")


      names.run(conn) must be === List("Ahmed","Maciek","Tammo")
    }

    it("finds from record") { conn =>

      val names = (limit:Int) => for {
        tammo <- query("select from customer where name = ?","Tammo")
        traverse <- Traverse.forIds(tammo.id)
          : WithConnectionI[Traverse]
        friend <- traverse.depth(limit).field("friends").iterate
      } yield (friend \ "name" @@ classOf[String] getOrElse "noName")


      names(1).run(conn) must be === List("Tammo","Maciek")
      names(2).run(conn) must be === List("Tammo","Maciek","Ahmed")
    }


  }

}
