package com.moralyzr.magickr.security.core.ports.outgoing

import cats.data.EitherT
import cats.effect.IO
import com.moralyzr.magickr.security.core.errors.AuthError
import com.moralyzr.magickr.security.core.types.TokenType.Token

trait GenerateToken:
  def jwt(): EitherT[IO, AuthError, Token]
