package com.moralyzr.magickr.domain.adventurer.recruitment.core

import cats.data.EitherT
import cats.effect.implicits.*
import cats.effect.kernel.Async
import cats.implicits.*
import com.moralyzr.magickr.domain.adventurer.recruitment.core.commands.{
  RecruitNewAdventurerCommand,
  UpdateAdventurerInfoCommand,
}
import com.moralyzr.magickr.domain.adventurer.recruitment.core.errors.{
  AdventurerNotFound,
  FailedToUpdateAdventurer,
}
import com.moralyzr.magickr.domain.adventurer.recruitment.core.mappers.AdventurerMapper
import com.moralyzr.magickr.domain.adventurer.recruitment.core.models.Adventurer
import com.moralyzr.magickr.domain.adventurer.recruitment.core.ports.incoming.{
  RecruitAdventurer,
  UpdateAdventurerInfo,
}
import com.moralyzr.magickr.domain.adventurer.recruitment.core.ports.outgoing.{FindAdventurer, PersistAdventurer}
import com.moralyzr.magickr.domain.adventurer.recruitment.core.validations.AdventurerValidationAlgebra
import com.moralyzr.magickr.domain.security.core.validations.UserValidationAlgebra
import com.moralyzr.magickr.infrastructure.errorhandling.Problem

class AdventurerBarracks[F[_]: Async](
  private val findAdventurer: FindAdventurer[F],
  private val persistAdventurer: PersistAdventurer[F],
  private val adventurerValidationAlgebra: AdventurerValidationAlgebra[F],
  private val userValidationAlgebra: UserValidationAlgebra[F],
) extends RecruitAdventurer[F]
    with UpdateAdventurerInfo[F]:

  override def recruit(
    userId: Long,
    command: RecruitNewAdventurerCommand,
  ): EitherT[F, Problem, Adventurer] = for {
    _ <- userValidationAlgebra.shouldExist(userId)
    _ <- adventurerValidationAlgebra.doesNotExistForUser(userId)
    adventurerToCreate = AdventurerMapper.fromRecruitCommand(userId, command)
    persistedAdventurer <- EitherT.liftF(persistAdventurer.save(adventurerToCreate))
  } yield persistedAdventurer

  override def update(
    userId: Long,
    command: UpdateAdventurerInfoCommand,
  ): EitherT[F, Problem, Adventurer] = for {
    adventurer <- findAdventurer.forUser(userId).toRight(new AdventurerNotFound())
    adventurerToUpdate = AdventurerMapper.fromUpdateInfoCommand(adventurer, command)
    updatedAdventurer <-
      persistAdventurer.update(adventurerToUpdate).toRight(new FailedToUpdateAdventurer())
  } yield updatedAdventurer

object AdventurerBarracks:

  def apply[F[_]: Async](
    findAdventurer: FindAdventurer[F],
    persistAdventurer: PersistAdventurer[F],
    adventurerValidationAlgebra: AdventurerValidationAlgebra[F],
    userValidationAlgebra: UserValidationAlgebra[F],
  ) = new AdventurerBarracks[F](
    findAdventurer,
    persistAdventurer,
    adventurerValidationAlgebra,
    userValidationAlgebra,
  )
