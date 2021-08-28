package com.moralyzr.magickr.security.core.ports.incoming

import cats.data.EitherT
import cats.effect.IO
import com.moralyzr.magickr.security.core.errors.AuthError
import com.moralyzr.magickr.security.core.types.EmailType.Email
import com.moralyzr.magickr.security.core.types.TokenType.*

trait LoginUserByCredentials[F[_]]:
  def login(command: LoginUserByCredentialsCommand): F[Either[AuthError, Token]]

final case class LoginUserByCredentialsCommand(
  val email   : Email,
  val password: String
)
