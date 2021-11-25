package com.moralyzr.magickr.domain.adventurer.development.core

import cats.{Applicative, Monad}
import cats.data.EitherT
import cats.effect.kernel.Async
import com.moralyzr.magickr.domain.adventurer.development.core.commands.DeduceExperienceCommand
import com.moralyzr.magickr.domain.adventurer.development.core.errors.{
  AdventurerLevelNotFound,
  LevelInformationNotFound,
}
import com.moralyzr.magickr.domain.adventurer.development.core.events.{
  AdventurerLeveledDown,
  AdventurerLeveledUp,
}
import com.moralyzr.magickr.domain.adventurer.development.core.models.AdventurerLevel
import com.moralyzr.magickr.domain.adventurer.development.core.ports.incoming.ManageAdventurerExperience
import com.moralyzr.magickr.domain.adventurer.development.core.ports.outgoing.{
  FindAdventurerLevel,
  FindLevelInfo,
  PersistAdventurerLevel,
  PublishAdventurerLevelingEvent,
}
import com.moralyzr.magickr.domain.adventurer.recruitment.core.commands.AwardAdventurerExperienceCommand
import com.moralyzr.magickr.infrastructure.errorhandling.Problem

class ExperienceManager[F[_]: Applicative: Monad](
  private val levelingEvent: PublishAdventurerLevelingEvent[F],
  private val findAdventurerLevel: FindAdventurerLevel[F],
  private val findLevelInfo: FindLevelInfo[F],
  private val adventurerLevelPersistence: PersistAdventurerLevel[F],
) extends ManageAdventurerExperience[F]:

  override def awardExperience(
    command: AwardAdventurerExperienceCommand,
  ): EitherT[F, Problem, AdventurerLevel] = for {
    adventurerLevel <- findAdventurerLevel
                         .withAdventurerId(command.adventurerId)
                         .toRight(AdventurerLevelNotFound())
    expUpdatedAdventurer = adventurerLevel.awardExperience(command.awardedExperience)
    nearestLevel <- findLevelInfo
                      .closestWithExperienceLowerThan(expUpdatedAdventurer.currentExperience)
                      .toRight(LevelInformationNotFound())
    (levelUpdatedAdventurer, event) = CheckAdventurerLevel.ifNeedUpdate(expUpdatedAdventurer, nearestLevel)
    persistedAdventurerLevel <- EitherT.liftF(adventurerLevelPersistence.save(levelUpdatedAdventurer))
    _ = event match {
          case leveledDownEvent @ AdventurerLeveledDown(_, _, _) => levelingEvent.leveledDown(leveledDownEvent)
          case leveledUpEvent @ AdventurerLeveledUp(_, _, _) => levelingEvent.leveledUp(leveledUpEvent)
          case other                                         => EitherT.pure(null)
        }
  } yield persistedAdventurerLevel
