package com.moralyzr.magickr.security.core.validations

import cats.data.EitherT
import cats.effect.IO
import com.moralyzr.magickr.security.core.errors.AuthError
import com.moralyzr.magickr.security.core.types.PasswordType.Password

trait PasswordValidationAlgebra:
  def invalidPassword(
      password: String,
      existingPassword: Password
  ): Either[AuthError, Unit]
