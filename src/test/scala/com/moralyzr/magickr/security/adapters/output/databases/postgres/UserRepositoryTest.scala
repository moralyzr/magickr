package com.moralyzr.magickr.security.adapters.output.databases.postgres

import org.scalatest.flatspec.AnyFlatSpec
import com.moralyzr.magickr.infrastructure.database.DatabaseConfig
import com.moralyzr.magickr.helpers.database.DatabaseTestConnectionHelper
import cats.effect.IO
import com.moralyzr.magickr.security.core.types.EmailType
import cats.effect.unsafe.implicits.global
import org.scalatest.matchers.should.Matchers.shouldBe
import com.moralyzr.magickr.security.core.models.User
import com.moralyzr.magickr.security.core.types.PasswordType
import java.time.LocalDate

class UserRepositoryTest extends AnyFlatSpec {
  behavior of "An User Repository"

  val databaseHelper = DatabaseTestConnectionHelper.buildTransactor()

  it should "fetch an user from the database, and if it does not exists, returns empty optional" in {
    val queryResult = databaseHelper
      .use { tx =>
        val userRepository = UserRepository[IO](tx)
        val result = userRepository.withEmail(EmailType.fromString("a@a.com"))
        result.value
      }
      .unsafeRunSync()

    queryResult.isEmpty shouldBe true
  }

  it should "fetch an user from the database, and if it exists, returns the user" in {
    val user = User(
      id = 1L,
      name = "Test",
      lastName = "Tester",
      email = EmailType.fromString("a@a.com"),
      password = PasswordType.fromString("test"),
      active = false,
      birthDate = LocalDate.now()
    )

    val queryResult = databaseHelper
      .use { tx =>
        val userRepository = UserRepository[IO](tx)
        val savedUser = userRepository.save(user).unsafeRunSync()
        val result = userRepository.withEmail(EmailType.fromString("a@a.com"))
        result.value
      }
      .unsafeRunSync()
    val savedUser = queryResult.get

    queryResult.isEmpty shouldBe false
    queryResult.contains(user) shouldBe true
  }

}
