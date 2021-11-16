package com.moralyzr.magickr.domain.adventurer.development.core.ports.input

import cats.data.EitherT
import com.moralyzr.magickr.domain.adventurer.development.core.commands.DeduceExperienceCommand
import com.moralyzr.magickr.domain.adventurer.development.core.models.AdventurerLevel
import com.moralyzr.magickr.domain.adventurer.recruitment.core.commands.AwardAdventurerExperienceCommand
import com.moralyzr.magickr.infrastructure.errorhandling.Problem

trait ManageAdventurerExperience[F[_]]:

  def awardExperience(
    command: AwardAdventurerExperienceCommand,
  ): EitherT[F, Problem, AdventurerLevel]

  def deduceExperience(
    command: DeduceExperienceCommand,
  ): EitherT[F, Problem, DeduceExperienceCommand]
