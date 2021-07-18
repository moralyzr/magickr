package com.moralyzr.magickr.security.adapters.jwt.internal

import com.moralyzr.magickr.security.core.ports.outgoing.JwtManager
import cats.data.EitherT
import cats.effect.IO
import pdi.jwt.{JwtAlgorithm, JwtCirce, JwtClaim}
import com.moralyzr.magickr.security.core.types.EmailType.Email
import com.moralyzr.magickr.security.core.errors.AuthError
import com.moralyzr.magickr.security.core.types.{JwtConfig, TokenType}
import com.moralyzr.magickr.security.core.types.TokenType.Token

import java.time.Instant

class JwtBuilder(val jwtConfig: JwtConfig) extends JwtManager :
  override def generate(userEmail: Email): Either[AuthError, Token] =
    val claim = JwtClaim(
      expiration = Some(Instant.now.plusSeconds(jwtConfig.expirationTime).getEpochSecond),
      issuedAt = Some(Instant.now.getEpochSecond),
      issuer = Some(jwtConfig.issuer),
      subject = Some(userEmail.toString),
    )
    val token = JwtCirce.encode(claim, jwtConfig.key, JwtAlgorithm.fromString(jwtConfig.algorithm))
    Right(TokenType.fromString(token))
