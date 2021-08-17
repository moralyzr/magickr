package com.moralyzr.magickr.security.core.validations

import cats.data.EitherT
import cats.Monad
import com.moralyzr.magickr.security.core.errors.AuthError

trait UserValidationAlgebra[F[_] : Monad]:
  def exists(userId: Long): EitherT[F, AuthError, Unit]
  def exists(email: String): EitherT[F, AuthError, Unit]
