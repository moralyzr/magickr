package com.moralyzr.magickr.domain.adventurer.development.core

import cats.data.EitherT
import cats.effect.kernel.Async
import com.moralyzr.magickr.domain.adventurer.development.core.commands.DeduceExperienceCommand
import com.moralyzr.magickr.domain.adventurer.development.core.models.AdventurerLevel
import com.moralyzr.magickr.domain.adventurer.development.core.ports.input.ManageAdventurerExperience
import com.moralyzr.magickr.domain.adventurer.development.core.ports.output.PublishAdventurerLevelingEvent
import com.moralyzr.magickr.domain.adventurer.recruitment.core.commands.AwardAdventurerExperienceCommand
import com.moralyzr.magickr.infrastructure.errorhandling.Problem

class ExperienceManager[F[_]: Async](
  private val publishAdventurerLevelingEvent: PublishAdventurerLevelingEvent[F],
) extends ManageAdventurerExperience[F]:

  override def awardExperience(
    command: AwardAdventurerExperienceCommand,
  ): EitherT[F, Problem, AdventurerLevel] = ???

  override def deduceExperience(
    command: DeduceExperienceCommand,
  ): EitherT[F, Problem, DeduceExperienceCommand] = ???
