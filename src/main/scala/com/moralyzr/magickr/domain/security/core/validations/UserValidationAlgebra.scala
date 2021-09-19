package com.moralyzr.magickr.domain.security.core.validations

import cats.data.EitherT
import cats.Monad
import com.moralyzr.magickr.domain.security.core.errors.{UserAlreadyExists, UserNotFound}

trait UserValidationAlgebra[F[_] : Monad]:
  def shouldExist(userId: Long): EitherT[F, UserNotFound, Unit]

  def shouldExist(email: String): EitherT[F, UserNotFound, Unit]

  def doesNotExist(userId: Long): EitherT[F, UserAlreadyExists, Unit]

  def doesNotExist(email: String): EitherT[F, UserAlreadyExists, Unit]
