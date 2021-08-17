package com.moralyzr.magickr.security.core.interpreters

import cats.data.EitherT
import cats.effect.IO
import cats.Monad
import com.github.t3hnar.bcrypt.*
import com.moralyzr.magickr.security.core.errors.AuthError
import com.moralyzr.magickr.security.core.types.PasswordType.Password
import com.moralyzr.magickr.security.core.validations.PasswordValidationAlgebra

class PasswordValidationInterpreter extends PasswordValidationAlgebra:
  override def invalidPassword(
    password: String,
    existingPassword: Password
  ): Either[AuthError, Unit] =
    password.isBcryptedBounded(existingPassword.toString) match {
      case true  => Right(())
      case false => Left(AuthError.InvalidCredentials)
    }
