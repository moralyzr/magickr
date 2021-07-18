package com.moralyzr.magickr.security.core

import cats.data.EitherT
import cats.effect.IO
import cats.{Functor, Monad}
import com.moralyzr.magickr.security.core.errors.AuthError
import com.moralyzr.magickr.security.core.models.User
import com.moralyzr.magickr.security.core.ports.incoming.LoginUserByCredentialsCommand
import com.moralyzr.magickr.security.core.ports.outgoing.{Authentication, FindUser}
import com.moralyzr.magickr.security.core.types.TokenType.Token

class SecurityManagement(
  private val findUser: FindUser,
  private val authentication: Authentication,
):
  def loginWithCredentials(command: LoginUserByCredentialsCommand)()(implicit F: Functor[IO]): EitherT[IO, AuthError, Token] =
    for {
      user <- findUser.withEmail(command.email).toRight(AuthError.UserNotFound)
      token <- EitherT.fromEither(authentication.forUser(user, command.password))
    } yield token
