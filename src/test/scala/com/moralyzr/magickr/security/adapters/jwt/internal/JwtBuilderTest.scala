package com.moralyzr.magickr.security.adapters.jwt.internal

import com.moralyzr.magickr.security.core.types.{EmailType, JwtConfig, TokenType}
import com.moralyzr.magickr.security.core.types.TokenType.Token
import org.scalatest.flatspec.AnyFlatSpec
import pdi.jwt.JwtAlgorithm

class JwtBuilderTest extends AnyFlatSpec {
  behavior of "The Internal Jwt Builder"

  private val jwtConfig = new JwtConfig(
    algorithm = JwtAlgorithm.HS256.name,
    expirationTime = 60,
    key = "jwtKey",
    issuer = "local",
  )

  it should "generate a valid Jwt Token based on configurations" in {
    val jwtBuilder = new JwtBuilder(jwtConfig)

    val tokenResult = jwtBuilder.generate(userEmail = EmailType.fromString("test@test.com"))

    assert(tokenResult.isRight)
    assert(tokenResult.getOrElse(TokenType.fromString("")) != TokenType.fromString(""))
  }

}
