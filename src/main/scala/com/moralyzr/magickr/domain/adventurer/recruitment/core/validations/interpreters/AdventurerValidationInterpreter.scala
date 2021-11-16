package com.moralyzr.magickr.domain.adventurer.recruitment.core.validations.interpreters

import cats.data.EitherT
import cats.effect.kernel.Async
import cats.implicits.*
import com.moralyzr.magickr.domain.adventurer.recruitment.core.errors.{AdventurerAlreadyExists, AdventurerNotFound}
import com.moralyzr.magickr.domain.adventurer.recruitment.core.ports.outgoing.FindAdventurer
import com.moralyzr.magickr.domain.adventurer.recruitment.core.validations.AdventurerValidationAlgebra

class AdventurerValidationInterpreter[F[_] : Async](val findAdventurer: FindAdventurer[F])
  extends AdventurerValidationAlgebra[F] :

  override def shouldExist(adventurerId: Long): EitherT[F, AdventurerNotFound, Unit] =
    findAdventurer.withId(adventurerId)
      .toRight(new AdventurerNotFound())
      .void

  override def shouldExistForUser(userId: Long): EitherT[F, AdventurerNotFound, Unit] =
    findAdventurer
      .forUser(userId)
      .toRight(new AdventurerNotFound())
      .void

  override def doesNotExist(adventurerId: Long): EitherT[F, AdventurerAlreadyExists, Unit] =
    findAdventurer.withId(adventurerId)
      .map(_ => new AdventurerAlreadyExists())
      .toLeft(())

  override def doesNotExistForUser(userId: Long): EitherT[F, AdventurerAlreadyExists, Unit] =
    findAdventurer.forUser(userId)
      .map(_ => new AdventurerAlreadyExists())
      .toLeft(())

object AdventurerValidationInterpreter:
  def apply[F[_] : Async](findAdventurer: FindAdventurer[F]) = new AdventurerValidationInterpreter[F](findAdventurer)
