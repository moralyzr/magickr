package com.moralyzr.magickr.domain.adventurer.development.fakes

import cats.data.EitherT
import cats.Monad
import com.moralyzr.magickr.domain.adventurer.development.core.events.{
  AdventurerLeveledDown,
  AdventurerLeveledUp,
}
import com.moralyzr.magickr.domain.adventurer.development.core.ports.outgoing.PublishAdventurerLevelingEvent
import com.moralyzr.magickr.infrastructure.errorhandling.Problem


class FakePublishAdventurerLevelingEvent[F[_]: Monad](
  val leveledUpResult: () => EitherT[F, Problem, Unit],
  val leveledDownResult: () => EitherT[F, Problem, Unit],
) extends PublishAdventurerLevelingEvent[F]:

  override def leveledUp(event: AdventurerLeveledUp): EitherT[F, Problem, Unit] = leveledUpResult.apply()

  override def leveledDown(event: AdventurerLeveledDown): EitherT[F, Problem, Unit] = leveledDownResult.apply()
