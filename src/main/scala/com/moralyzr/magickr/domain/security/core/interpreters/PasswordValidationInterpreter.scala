package com.moralyzr.magickr.domain.security.core.interpreters

import cats.data.EitherT
import cats.effect.IO
import cats.Monad
import com.github.t3hnar.bcrypt.*
import com.moralyzr.magickr.domain.security.core.errors.{AuthError, InvalidCredentials}
import com.moralyzr.magickr.domain.security.core.validations.PasswordValidationAlgebra
import com.moralyzr.magickr.domain.security.core.errors.InvalidCredentials
import com.moralyzr.magickr.domain.security.core.types.PasswordType.Password

class PasswordValidationInterpreter extends PasswordValidationAlgebra :
  override def invalidPassword(
    password        : String,
    existingPassword: Password
  ): Either[AuthError, Unit] =
    password.isBcryptedBounded(existingPassword.toString) match {
      case true  => Right(())
      case false => Left(new InvalidCredentials())
    }
