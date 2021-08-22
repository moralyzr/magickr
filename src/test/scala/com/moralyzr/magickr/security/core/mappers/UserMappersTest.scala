package com.moralyzr.magickr.security.core.mappers

import com.moralyzr.magickr.security.core.interpreters.PasswordValidationInterpreter
import com.moralyzr.magickr.security.core.ports.incoming.RegisterUserWithCredentialsCommand
import com.moralyzr.magickr.security.core.types.{EmailType, PasswordType}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.time.LocalDate

class UserMappersTest extends AnyFlatSpec with Matchers {
  behavior of "An user mapper"

  private val passwordValidator = new PasswordValidationInterpreter()

  it should "Map an user RegisterUserWithCredentialsCommand to an User Object" in {
    val command = RegisterUserWithCredentialsCommand(
      name = "John",
      lastName = "Doe",
      email = "a@a.com",
      password = "pass",
      birthDate = LocalDate.now()
    )

    val user = UserMappers.from(command)

    user.name shouldBe command.name
    user.lastName shouldBe command.lastName
    passwordValidator
      .invalidPassword(
        password = "pass",
        existingPassword = PasswordType.fromString("pass")
      )
      .isRight shouldBe true
    user.email shouldBe EmailType.fromString(command.email)
    user.birthDate shouldBe command.birthDate
    user.active shouldBe false
  }

}
