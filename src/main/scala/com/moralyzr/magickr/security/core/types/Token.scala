package com.moralyzr.magickr.security.core.types

final case class JwtConfig(
  algorithm: String,
  expirationTime: Int,
  key: String,
  issuer: String,
)

object TokenType:
  opaque type Token = String

  def fromString(token: String): Token = token
