package com.moralyzr.magickr.security.adapters.output.internal

import com.moralyzr.magickr.security.adapters.output.security.internal.{InternalAuthentication, JwtBuilder}
import com.moralyzr.magickr.security.core.interpreters.SecurityValidationsInterpreter
import com.moralyzr.magickr.security.core.models.User
import com.moralyzr.magickr.security.core.ports.outgoing.FindUser
import com.moralyzr.magickr.security.core.types.{EmailType, JwtConfig, PasswordType}
import com.moralyzr.magickr.security.core.validations.PasswordValidationAlgebra
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.BeforeAndAfterEach
import pdi.jwt.JwtAlgorithm
import com.moralyzr.magickr.security.core.errors.AuthError

import java.time.LocalDate

class InternalAuthenticationTest extends AnyFlatSpec :
  behavior of "The embedded authentication mechanism"

  private val jwtConfig = new JwtConfig(
    algorithm = JwtAlgorithm.HS256.name,
    expirationTime = 60,
    key = "jwtKey",
    issuer = "local",
  )
  private val passwordValidationAlgebra = SecurityValidationsInterpreter
  private val jwtBuilder = new JwtBuilder(jwtConfig)
  private val internalAuthentication = new InternalAuthentication(passwordValidation = passwordValidationAlgebra, jwtManager = jwtBuilder)

  it should "return a token if the user password matches" in {
    val user = User(
      name = "Test",
      lastName = "Tester",
      email = EmailType.fromString("a@a.com"),
      password = PasswordType.fromString("test"),
      active = true,
      birthDate = LocalDate.now(),
    )

    val auth = internalAuthentication.forUser(user = user, password = "test")

    assert(auth.isRight)
  }

  it should "return a failure if the password does not matches" in {
    val user = User(
      name = "Test",
      lastName = "Tester",
      email = EmailType.fromString("a@a.com"),
      password = PasswordType.fromString("test"),
      active = true,
      birthDate = LocalDate.now(),
    )

    val auth = internalAuthentication.forUser(user = user, password = "test2")

    assert(auth.isLeft)
    assert(auth.left.exists(error => error == AuthError.InvalidCredentials))
  }
