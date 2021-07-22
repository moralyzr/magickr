package com.moralyzr.magickr.security.core

import cats.data.EitherT
import cats.effect.kernel.{Resource, Async}
import com.moralyzr.magickr.security.core.errors.AuthError
import com.moralyzr.magickr.security.core.models.User
import com.moralyzr.magickr.security.core.ports.incoming.LoginUserByCredentialsCommand
import com.moralyzr.magickr.security.core.ports.outgoing.{Authentication, FindUser}
import com.moralyzr.magickr.security.core.types.TokenType.Token

class SecurityManagement[F[_] : Async](
  private val findUser: FindUser[F],
  private val authentication: Authentication[F],
):
  def loginWithCredentials(command: LoginUserByCredentialsCommand)(): EitherT[F, AuthError, Token] =
    for {
      user <- findUser.withEmail(command.email).toRight(AuthError.UserNotFound)
      token <- EitherT.fromEither(authentication.forUser(user, command.password))
    } yield token

object SecurityManagement:
  def apply[F[_] : Async](findUser: FindUser[F], authentication: Authentication[F]): SecurityManagement[F] =
    new SecurityManagement[F](findUser, authentication)
