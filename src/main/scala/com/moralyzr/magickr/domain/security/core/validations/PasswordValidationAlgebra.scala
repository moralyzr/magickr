package com.moralyzr.magickr.domain.security.core.validations

import cats.data.EitherT
import cats.effect.IO
import com.moralyzr.magickr.domain.security.core.errors.AuthError
import com.moralyzr.magickr.domain.security.core.types.PasswordType.Password

trait PasswordValidationAlgebra:
  def invalidPassword(
    password        : String,
    existingPassword: Password
  ): Either[AuthError, Unit]
