package com.moralyzr.magickr.security.core.ports.outgoing

import cats.effect.IO
import com.moralyzr.magickr.security.core.errors.AuthError
import com.moralyzr.magickr.security.core.types.EmailType.Email
import com.moralyzr.magickr.security.core.types.TokenType.Token

trait JwtManager[F[_]]:
  def generate(userEmail: Email): Either[AuthError, Token]
