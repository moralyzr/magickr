package com.moralyzr.magickr.domain.adventurer.development.core.ports.outgoing

import cats.data.OptionT
import com.moralyzr.magickr.domain.adventurer.development.core.models.LevelInfo

trait FindLevelInfo[F[_]]:
  def withId(id: Long): OptionT[F, LevelInfo]
  def closestWithExperienceLowerThan(exp: BigInt): OptionT[F, LevelInfo]
