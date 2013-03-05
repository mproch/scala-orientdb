package scala.orientdb.document

import org.scalatest.matchers.MustMatchers
import orientdb.WithTestDatabase
import orientdb.connection.monadic.{WithConnection, WithConnectionT, DocumentHelper}
import WithConnectionT._
import orientdb.connection.Connection
import scala.orientdb.types.OrientDBTypes._
import DocumentHelper._
import orientdb.types.{OId, OrientDBType}

class ODocumentPartSpec extends MustMatchers with WithTestDatabase {

  describe("ODocumentPart traversing") {

    it("gets embedded empty fields") { conn =>
      val value = getField[String](conn, _ \ "nonexis" \ "ala" \ "bela")

      value must be ('empty)
    }

    it("gets nonempty fields") { conn =>
      val value = getField[String](conn, _ \ "address" \ "street")

      value must be === Some("Popularna")
    }

    it("sets field on document level") { conn =>

      runAndSave(conn, _ \ "pesel" mod "1234567")

      val pesel = getField[String](conn, _ \ "pesel")

      pesel must be === Some("1234567")
    }


    it("sets field one level deep") { conn =>

      runAndSave(conn, _ \ "address" \ "street" mod "al Ken")

      val street = getField[String](conn, _ \ "address" \ "street")

      street must be === Some("al Ken")

    }

    it("adds topLevel field") { conn =>

      runAndSave(conn, _ \ "title" mod "Mr")

      val street = getField[String](conn, _ \ "title")

      street must be === Some("Mr")

    }

    it("adds nested field") { conn =>

      runAndSave(conn, _ \ "address" \ "country" mod "Poland")

      val street = getField[String](conn, _ \ "address" \ "country")

      street must be === Some("Poland")

    }

    it("adds nested field for notexisting path") { conn =>

      runAndSave(conn, _ \ "address2" \ "country" mod "Poland")

      val street = getField[String](conn, _ \ "address2" \ "country")

      street must be === Some("Poland")
    }


    it("returns value for list field") { conn =>

      val value = getField[String](conn, _ \\ "labels" get 1)
      value must be === Some("nice")
    }


    it("returns empty for nonexisting list field") { conn =>

      val value = getField[String](conn, _ \\ "nolist" get 0)
      val embeddedValue = getField[String](conn, _ \ "address" \\ "nolist" get 0)

      value must be ('empty)
      embeddedValue must be ('empty)

    }

    it("returns empty for nonexisting index") { conn =>

      val value = getField[String](conn, _ \\ "nolist" get 5)
      value must be ('empty)

    }


    it("modifies list field") { conn =>

      runAndSave(conn, _ \\ "labels" get 1 mod "not nice")

      val value = getField[String](conn, _ \\ "labels" get 1)
      value must be === Some("not nice")

    }


    it("adds field for nonexisting index") { conn =>
      runAndSave(conn, _ \\ "labels" get 2 mod "not nice")

      val value = getField[String](conn, _ \\ "labels" get 2)
      value must be === Some("not nice")

    }

    it("adds new list field") { conn =>
      runAndSave(conn, _ \\ "badLabels" get 0 mod "not nice")

      val value = getField[String](conn, _ \\ "badLabels" get 0)
      value must be === Some("not nice")

    }

    it("traverses links") { conn =>
      val friend1 = getField[OId](conn, _ \\ "friends" get 0)

      friend1 must be ('defined)

      val name = WithConnectionT.pointT(friend1).flatMap(_.get).map(_ \ "name").flatMap(_.@@[String]).run(conn)
      name must be === Some("Ahmed")
    }

    it("adds new link") { conn =>
      val tammoId = customerByName(conn, "Tammo", _.id)
      tammoId must be ('defined)

      runAndSave(conn, _ \\ "friends" get 1 mod tammoId.get)

      val friend1 = getField[OId](conn, _ \\ "friends" get 1)
      val name = WithConnectionT.pointT(friend1).flatMap(_.get).map(_ \ "name").flatMap(_.@@[String]).run(conn)
      name must be === Some("Tammo")

    }

  }

  private def save(doc:ODocumentS) = doc.save()

  private def getField[T:OrientDBType](conn:Connection,path:ODocumentS=>Traversable) =
    runOnMaciek(conn, path).flatMap(_.@@[T])

  private def runAndSave[T](conn:Connection, action:ODocumentS=>ODocumentS) =
    customerByNameT(conn, "Maciek", action andThen save)

  private def runOnMaciek[T](conn:Connection, action:ODocumentS=>T) : Option[T]
    = customerByName(conn, "Maciek", action)

  private def customerByName[T](conn:Connection, name:String, action:ODocumentS=>T) : Option[T]=
    customerByNameT(conn, name, action andThen WithConnection.point)

  private def customerByNameT[T](conn:Connection, name:String, action:ODocumentS=>WithConnection[T]) : Option[T]=
    (for {
      customer <- point[Option,ODocumentS](
      DocumentHelper.query("select from customer where name = ?",name).map(_.headOption))
      result <- point[Option,T](action(customer).map(Some(_)))
    } yield (result)).run(conn)

}
