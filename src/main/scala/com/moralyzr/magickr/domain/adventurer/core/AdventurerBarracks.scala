package com.moralyzr.magickr.domain.adventurer.core

import cats.data.EitherT
import cats.effect.implicits.*
import cats.effect.kernel.Async
import cats.implicits.*

import com.moralyzr.magickr.domain.adventurer.core.commands.RecruitNewAdventurerCommand
import com.moralyzr.magickr.domain.adventurer.core.mappers.AdventurerMapper
import com.moralyzr.magickr.domain.adventurer.core.ports.outgoing.FindAdventurer
import com.moralyzr.magickr.domain.adventurer.core.ports.outgoing.PersistAdventurer
import com.moralyzr.magickr.domain.adventurer.core.validations.AdventurerValidationAlgebra
import com.moralyzr.magickr.domain.security.core.validations.UserValidationAlgebra

class AdventurerBarracks[F[_] : Async](
  private val findAdventurer             : FindAdventurer[F],
  private val persistAdventurer          : PersistAdventurer[F],
  private val adventurerValidationAlgebra: AdventurerValidationAlgebra[F],
  private val userValidationAlgebra      : UserValidationAlgebra[F],
):
  def recruit(command: RecruitNewAdventurerCommand) = for {
    _ <- userValidationAlgebra.shouldExist(command.userId)
    _ <- adventurerValidationAlgebra.doesNotExistForUser(command.userId)
    adventurerToCreate = AdventurerMapper.fromRecruitCommand(command)
    persistedAdventurer <- EitherT.liftF(persistAdventurer.save(adventurerToCreate))
  } yield persistedAdventurer

