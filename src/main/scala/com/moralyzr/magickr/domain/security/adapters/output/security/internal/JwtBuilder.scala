package com.moralyzr.magickr.domain.security.adapters.output.security.internal

import cats.data.EitherT
import cats.effect.IO
import cats.effect.kernel.Sync
import com.moralyzr.magickr.domain.security.core.errors.AuthError
import com.moralyzr.magickr.domain.security.core.ports.outgoing.JwtManager
import com.moralyzr.magickr.domain.security.core.types.TokenType
import com.moralyzr.magickr.domain.security.core.types.EmailType.Email
import com.moralyzr.magickr.domain.security.core.types.{JwtConfig, TokenType}
import com.moralyzr.magickr.domain.security.core.types.TokenType.Token
import pdi.jwt.{JwtAlgorithm, JwtCirce, JwtClaim}

import java.time.Instant

class JwtBuilder[F[_] : Sync](val jwtConfig: JwtConfig) extends JwtManager[F] :
  override def generate(userEmail: Email): Either[AuthError, Token] =
    val claim = JwtClaim(
      expiration = Some(Instant.now.plusSeconds(jwtConfig.expirationTime).getEpochSecond),
      issuedAt = Some(Instant.now.getEpochSecond),
      issuer = Some(jwtConfig.issuer),
      subject = Some(userEmail.toString),
    )
    val token = JwtCirce.encode(claim, jwtConfig.key, JwtAlgorithm.fromString(jwtConfig.algorithm))
    Right(TokenType.fromString(token))

object JwtBuilder:
  def apply[F[_] : Sync](jwtConfig: JwtConfig) =
    new JwtBuilder[F](jwtConfig)
