package com.moralyzr.magickr.security.core.ports.outgoing

import cats.data.EitherT
import cats.effect.IO
import cats.Monad
import com.moralyzr.magickr.security.core.errors.AuthError
import com.moralyzr.magickr.security.core.models.User
import com.moralyzr.magickr.security.core.types.EmailType.Email
import com.moralyzr.magickr.security.core.types.PasswordType.Password
import com.moralyzr.magickr.security.core.types.TokenType.Token

trait Authentication[F[_]]:
  def forUser(user: User, password: String): Either[AuthError, Token]
