package com.moralyzr.magickr.domain.adventurer.recruitment.core.validations

import cats.data.EitherT
import com.moralyzr.magickr.domain.adventurer.recruitment.core.errors.AdventurerNotFound
import com.moralyzr.magickr.domain.adventurer.recruitment.core.errors.AdventurerAlreadyExists

trait AdventurerValidationAlgebra[F[_]]:
  def shouldExist(adventurerId: Long): EitherT[F, AdventurerNotFound, Unit]

  def shouldExistForUser(userId: Long): EitherT[F, AdventurerNotFound, Unit]

  def doesNotExist(adventurerId: Long): EitherT[F, AdventurerAlreadyExists, Unit]

  def doesNotExistForUser(userId: Long): EitherT[F, AdventurerAlreadyExists, Unit]
