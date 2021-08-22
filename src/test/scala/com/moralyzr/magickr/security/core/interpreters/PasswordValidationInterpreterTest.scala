package com.moralyzr.magickr.security.core.interpreters

import com.moralyzr.magickr.security.core.errors.InvalidCredentials
import com.moralyzr.magickr.security.core.types.PasswordType
import org.scalatest.flatspec.AnyFlatSpec

class PasswordValidationInterpreterTest extends AnyFlatSpec {
  behavior of "The Password security validation"

  private val interpreter = new PasswordValidationInterpreter()

  it should "validate a success if the password match the hash" in {
    val plainPassword = "plain-password"
    val hashedPassword = PasswordType.fromString(plainPassword)

    val result = interpreter.invalidPassword(plainPassword, hashedPassword)

    assert(result.isRight)
  }

  it should "return an invalid credentials error if the hash does not match" in {
    val plainPassword = "plain-password"
    val hashedPassword = PasswordType.fromString("another-password")

    val result = interpreter.invalidPassword(plainPassword, hashedPassword)

    assert(result.isLeft)
    assert(result.left.exists(e => e.equals(InvalidCredentials)))
  }

}
