package com.moralyzr.magickr.domain.security.core.validations

import cats.data.EitherT
import cats.Monad
import com.moralyzr.magickr.domain.security.core.errors.AuthError

trait UserValidationAlgebra[F[_] : Monad]:
  def shouldExist(userId: Long): EitherT[F, AuthError, Unit]

  def shouldExist(email: String): EitherT[F, AuthError, Unit]

  def doesNotExist(userId: Long): EitherT[F, AuthError, Unit]

  def doesNotExist(email: String): EitherT[F, AuthError, Unit]
