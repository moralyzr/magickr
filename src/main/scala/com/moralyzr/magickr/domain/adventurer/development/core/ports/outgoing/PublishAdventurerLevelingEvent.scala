package com.moralyzr.magickr.domain.adventurer.development.core.ports.outgoing

import cats.Monad
import cats.data.EitherT
import com.moralyzr.magickr.domain.adventurer.development.core.events.{
  AdventurerLeveledDown,
  AdventurerLeveledUp,
}
import com.moralyzr.magickr.infrastructure.errorhandling.Problem

trait PublishAdventurerLevelingEvent[F[_]: Monad]:
  def leveledUp(event: AdventurerLeveledUp): EitherT[F, Problem, Unit]
  def leveledDown(event: AdventurerLeveledDown): EitherT[F, Problem, Unit]
