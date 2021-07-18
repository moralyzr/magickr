package com.moralyzr.magickr.security.adapters.authentication.internal

import cats.data.{EitherT, *}
import cats.effect.IO
import cats.{Functor, Monad}
import cats.syntax.functor.*
import com.moralyzr.magickr.security.core.errors.AuthError
import com.moralyzr.magickr.security.core.models.User
import com.moralyzr.magickr.security.core.ports.outgoing.{AuthenticateUser, FindUser}
import com.moralyzr.magickr.security.core.types.EmailType.Email
import com.moralyzr.magickr.security.core.types.PasswordType.Password
import com.moralyzr.magickr.security.core.validations.PasswordValidationAlgebra

class Authentication(val findUser: FindUser, val passwordValidation: PasswordValidationAlgebra) extends AuthenticateUser :
  override def withCredentials(email: Email, password: String): EitherT[IO, AuthError, User] =
    for {
      user <- findUser.withEmail(email).toRight(AuthError.UserNotFound)
      _ <- EitherT.fromEither(passwordValidation.invalidPassword(password, user.password))
    } yield user
