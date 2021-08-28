package com.moralyzr.magickr.security.core.interpreters

import cats.effect.IO
import cats.implicits.*
import cats.effect.unsafe.implicits.global
import com.moralyzr.magickr.security.core.errors.{
  UserAlreadyExists,
  UserNotFound
}
import com.moralyzr.magickr.security.core.ports.outgoing.FindUser
import com.moralyzr.magickr.security.doubles.{FindUserNotFound, FindUserOk}
import com.moralyzr.magickr.security.fixtures.UserFixtures
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.EitherValues

class UserValidationInterpreterTest extends AnyFlatSpec
                                    with Matchers
                                    with EitherValues {

  behavior of "The user validation interpreter shouldExist"

  it should "return an unit if the user exists while searching by id" in {
    val findUserSuccess = new FindUserOk(() => UserFixtures.user)

    val userValidationInterpreter = UserValidationInterpreter[IO](findUserSuccess)

    val result = userValidationInterpreter.shouldExist(1L).value.unsafeRunSync()

    result.isRight shouldBe true
  }

  it should "return an unit if the user exists while searching by e-mail" in {
    val findUserSuccess           = new FindUserOk(() => UserFixtures.user)
    val userValidationInterpreter = UserValidationInterpreter[IO](findUserSuccess)

    val result = userValidationInterpreter.shouldExist("a@a.com").value.unsafeRunSync()

    result.isRight shouldBe true
  }

  it should "return an UserNotFound if the user not exists while searching by e-mail" in {
    val findUserNotFound          = new FindUserNotFound()
    val userValidationInterpreter = UserValidationInterpreter[IO](findUserNotFound)

    val result = userValidationInterpreter.shouldExist("a@a.com").value.unsafeRunSync()

    result.left.value shouldBe new UserNotFound()
  }

  behavior of "The user validation interpreter doesNotExist"

  it should "return an unit if the user not exists while searching by e-mail" in {
    val findUserNotFound          = new FindUserNotFound()
    val userValidationInterpreter = UserValidationInterpreter[IO](findUserNotFound)

    val result = userValidationInterpreter.doesNotExist("a@a.com").value.unsafeRunSync()

    result.isRight shouldBe true
  }

  it should "return an unit if the user not exists while searching by id" in {
    val findUserNotFound          = new FindUserNotFound()
    val userValidationInterpreter = UserValidationInterpreter[IO](findUserNotFound)

    val result = userValidationInterpreter.doesNotExist(1L).value.unsafeRunSync()

    result.isRight shouldBe true
  }

  it should "return an UserAlreadyExists if the user exists while searching by e-mail" in {
    val findUserNotFound          = new FindUserOk(() => UserFixtures.user)
    val userValidationInterpreter = UserValidationInterpreter[IO](findUserNotFound)

    val result = userValidationInterpreter.doesNotExist("a@a.com").value.unsafeRunSync()

    result.left.value shouldBe new UserAlreadyExists()
  }

  it should "return an UserAlreadyExists if the user exists while searching by id" in {
    val findUserNotFound          = new FindUserOk(() => UserFixtures.user)
    val userValidationInterpreter = UserValidationInterpreter[IO](findUserNotFound)

    val result = userValidationInterpreter.doesNotExist(1L).value.unsafeRunSync()

    result.left.value shouldBe new UserAlreadyExists()
  }

}
