package com.moralyzr.magickr.domain.security.core.types

import cats.effect.IO
import cats.effect.kernel.Sync
import com.typesafe.config.Config

final case class JwtConfig(
  algorithm     : String,
  expirationTime: Int,
  key           : String,
  issuer        : String
)

object JwtConfig:
  def apply[F[_] : Sync](config: Config) = new JwtConfig(
    algorithm = config.getString("server.http.security.jwt.algorithm"),
    expirationTime = config.getInt("server.http.security.jwt.expiration-time"),
    key = config.getString("server.http.security.jwt.key"),
    issuer = config.getString("server.http.security.jwt.issuer")
  )

object TokenType:
  opaque type Token = String

  def fromString(token: String): Token = token
