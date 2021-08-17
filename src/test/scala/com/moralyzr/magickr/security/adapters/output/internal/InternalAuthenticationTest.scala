package com.moralyzr.magickr.security.adapters.output.internal

import com.moralyzr.magickr.security.adapters.output.security.internal.{
  InternalAuthentication,
  JwtBuilder
}
import com.moralyzr.magickr.security.core.interpreters.PasswordValidationInterpreter
import com.moralyzr.magickr.security.core.models.User
import com.moralyzr.magickr.security.core.ports.outgoing.FindUser
import com.moralyzr.magickr.security.core.types.{
  EmailType,
  JwtConfig,
  PasswordType
}
import com.moralyzr.magickr.security.core.validations.PasswordValidationAlgebra
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.BeforeAndAfterEach
import pdi.jwt.JwtAlgorithm
import com.moralyzr.magickr.security.core.errors.AuthError
import cats.effect.kernel.Sync

import java.time.LocalDate
import cats.effect.IO
import cats.effect.unsafe.implicits.global
import org.scalatest.matchers.should.Matchers.*

class InternalAuthenticationTest extends AnyFlatSpec:
  behavior of "The embedded authentication mechanism"

  private val jwtConfig = new JwtConfig(
    algorithm = JwtAlgorithm.HS256.name,
    expirationTime = 60,
    key = "jwtKey",
    issuer = "local"
  )

  private val passwordValidationAlgebra = new PasswordValidationInterpreter()
  private val jwtBuilder = JwtBuilder[IO](jwtConfig)
  private val internalAuthentication = InternalAuthentication[IO](
    passwordValidationAlgebra = passwordValidationAlgebra,
    jwtManager = jwtBuilder
  )

  it should "return a token if the user password matches" in {
    val user = User(
      id = Some(1L),
      name = "Test",
      lastName = "Tester",
      email = EmailType.fromString("a@a.com"),
      password = PasswordType.fromString("test"),
      active = true,
      birthDate = LocalDate.now()
    )

    val result = internalAuthentication
      .forUser(user = user, password = "test")
      .value
      .unsafeRunSync()

    result.isRight shouldBe true
  }

  it should "return a failure if the password does not matches" in {
    val user = User(
      id = Some(1L),
      name = "Test",
      lastName = "Tester",
      email = EmailType.fromString("a@a.com"),
      password = PasswordType.fromString("test"),
      active = true,
      birthDate = LocalDate.now()
    )

    val result = internalAuthentication
      .forUser(user = user, password = "test2")
      .value
      .unsafeRunSync()

    result.isLeft shouldBe true  
  }
