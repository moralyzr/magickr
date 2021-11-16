package com.moralyzr.magickr.domain.adventurer.development.core.ports.output

import cats.data.EitherT
import com.moralyzr.magickr.domain.adventurer.development.core.events.{
  AdventurerLeveledDown,
  AdventurerLeveledUp,
}
import com.moralyzr.magickr.infrastructure.errorhandling.Problem

trait PublishAdventurerLevelingEvent[F[_]]:
  def leveledUp(event: AdventurerLeveledUp): EitherT[F, Problem, Nothing]
  def leveledDown(event: AdventurerLeveledDown): EitherT[F, Problem, Nothing]
