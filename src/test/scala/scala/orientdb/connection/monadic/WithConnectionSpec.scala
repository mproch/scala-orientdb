package scala.orientdb.connection.monadic

import orientdb.WithTestDatabase

class WithConnectionSpec extends WithTestDatabase {

  describe("WithConnection monad") {

    it("queries connection") { connection =>

      val saved = for {
        customer <- DocumentHelper.newInstance("customer")
        id <- customer.save()
      } yield (id)

      saved.run(connection)



    }

  }

}
