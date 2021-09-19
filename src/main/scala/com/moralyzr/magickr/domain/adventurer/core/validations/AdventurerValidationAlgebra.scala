package com.moralyzr.magickr.domain.adventurer.core.validations

import cats.data.EitherT
import com.moralyzr.magickr.domain.adventurer.core.errors.{AdventurerAlreadyExists, AdventurerNotFound}
import com.moralyzr.magickr.domain.security.core.errors.AuthError

trait AdventurerValidationAlgebra[F[_]]:
  def shouldExist(adventurerId: Long): EitherT[F, AdventurerNotFound, Unit]

  def shouldExistForUser(userId: Long): EitherT[F, AdventurerNotFound, Unit]

  def doesNotExist(adventurerId: Long): EitherT[F, AdventurerAlreadyExists, Unit]

  def doesNotExistForUser(userId: Long): EitherT[F, AdventurerAlreadyExists, Unit]
