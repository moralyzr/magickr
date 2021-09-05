package com.moralyzr.magickr.domain.security.adapters.output.databases.postgres

import org.scalatest.flatspec.AnyFlatSpec
import com.moralyzr.magickr.infrastructure.database.DatabaseConfig
import com.moralyzr.magickr.helpers.database.DatabaseTestConnectionHelper
import cats.effect.IO
import cats.effect.kernel.Resource
import cats.effect.unsafe.implicits.global
import com.moralyzr.magickr.domain.security.core.models.User
import com.moralyzr.magickr.domain.security.core.types.{EmailType, PasswordType}
import org.scalatest.matchers.should.Matchers.shouldBe
import org.scalatest.matchers.should.Matchers

import java.time.LocalDate

class UserRepositoryTest extends AnyFlatSpec with Matchers {
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
      id = Some(1L),
      name = "Test",
      lastName = "Tester",
      email = EmailType.fromString("a@a.com"),
      password = PasswordType.fromString("test"),
      active = false,
      birthDate = LocalDate.now()
    )

    val theUser = (for {
      transactor <- databaseHelper
      userRepository = UserRepository[IO](transactor)
      _ <- Resource.eval(userRepository.save(user))
      foundUser = userRepository.withEmail(EmailType.fromString("a@a.com"))
    } yield foundUser).use(_.value).unsafeRunSync()

    theUser should contain(user)
  }

  it should "update an user if it exists" in {
    val user = User(
      id = Some(2L),
      name = "Test",
      lastName = "Tester",
      email = EmailType.fromString("a@a.com"),
      password = PasswordType.fromString("test"),
      active = false,
      birthDate = LocalDate.now()
    )

    val expectedUpdatedUser = user.copy(active = true)

    val theUpdatedUser = (for {
      transactor <- databaseHelper
      userRepository = UserRepository[IO](transactor)
      savedUser <- Resource.eval(userRepository.save(user))
      userToUpdate = savedUser.copy(active = true)
      updatedUser = userRepository.update(userToUpdate)
    } yield updatedUser).use(_.value).unsafeRunSync()

    theUpdatedUser should contain(expectedUpdatedUser)
  }

}
