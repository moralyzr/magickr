package com.moralyzr.magickr.domain.security.core.ports.incoming

import cats.data.EitherT
import cats.effect.IO
import com.moralyzr.magickr.domain.security.core.errors.AuthError
import com.moralyzr.magickr.domain.security.core.types.EmailType.Email
import com.moralyzr.magickr.domain.security.core.types.TokenType.*

trait LoginUserByCredentials[F[_]]:
  def login(command: LoginUserByCredentialsCommand): EitherT[F, AuthError, Token]

final case class LoginUserByCredentialsCommand(
  val email   : Email,
  val password: String
)
