package com.moralyzr.magickr.security.adapters.output.databases.postgres

import org.scalatest.flatspec.AnyFlatSpec
import com.moralyzr.magickr.infrastructure.database.DatabaseConfig
import com.moralyzr.magickr.helpers.database.DatabaseTestConnectionHelper
import cats.effect.IO
import com.moralyzr.magickr.security.core.types.EmailType
import cats.effect.unsafe.implicits.global
import org.scalatest.matchers.should.Matchers.shouldBe

class UserRepositoryTest extends AnyFlatSpec {
  behavior of "An User Repository"

  val databaseHelper = DatabaseTestConnectionHelper.buildTransactor()

  it should "fetch an user from the database, and if it does not exists, returns empty optional" in {
    val queryResult = databaseHelper.use { tx =>   
        val userRepository = UserRepository[IO](tx)
        val result = userRepository.withEmail(EmailType.fromString("a@a.com"))
        result.value
    }.unsafeRunSync()

    queryResult.isEmpty shouldBe true
  }

}
