package com.moralyzr.magickr.domain.security.adapters.output.internal

import com.moralyzr.magickr.domain.security.core.types.{EmailType, JwtConfig, TokenType}
import com.moralyzr.magickr.domain.security.core.types.TokenType.Token
import org.scalatest.flatspec.AnyFlatSpec
import pdi.jwt.JwtAlgorithm
import cats.effect.kernel.Sync
import cats.effect.kernel.Resource
import cats.effect.IO
import cats.data.EitherT
import org.scalatest.matchers.should.Matchers.shouldBe

import scala.concurrent.duration.*
import cats.effect.unsafe.implicits.global
import com.moralyzr.magickr.domain.security.adapters.output.security.internal.JwtBuilder
import com.moralyzr.magickr.domain.security.core.types.{EmailType, JwtConfig}
import pdi.jwt.JwtCirce
import pdi.jwt.JwtClaim

class JwtBuilderTest extends AnyFlatSpec {
  behavior of "The Internal Jwt Builder"

  private val jwtConfig = new JwtConfig(
    algorithm = JwtAlgorithm.HS256.name,
    expirationTime = 60,
    key = "jwtKey",
    issuer = "local"
  )

  private val jwtBuilder = JwtBuilder[IO](jwtConfig)

  it should "generate a valid Jwt Token based on configurations" in {
    val generatedToken =
      jwtBuilder.generate(EmailType.fromString("a@a.com")).getOrElse(null)

    val decodedToken = JwtCirce
      .decode(generatedToken.toString, jwtConfig.key, Seq(JwtAlgorithm.HS256))
      .get

    decodedToken.issuer shouldBe Some(jwtConfig.issuer)
  }
}
