package com.moralyzr.magickr.domain.adventurer.recruitment.core.ports.incoming

import cats.data.EitherT
import com.moralyzr.magickr.domain.adventurer.recruitment.core.commands.{
  AwardAdventurerExperienceCommand,
  DeduceAdventurerExperienceCommand,
}
import com.moralyzr.magickr.domain.adventurer.recruitment.core.models.Adventurer
import com.moralyzr.magickr.infrastructure.errorhandling.Problem

trait UpdateAdventurerExperience[F[_]]:
  def awardExperience(command: AwardAdventurerExperienceCommand): EitherT[F, Problem, Adventurer]
  def deduceExperience(command: DeduceAdventurerExperienceCommand): EitherT[F, Problem, Adventurer]
