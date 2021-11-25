package com.moralyzr.magickr.domain.adventurer.development.fakes

import cats.data.OptionT
import com.moralyzr.magickr.domain.adventurer.development.core.models.LevelInfo
import com.moralyzr.magickr.domain.adventurer.development.core.ports.outgoing.FindLevelInfo

class FakeFindLevelInfo[F[_]](
  withIdResponse: () => OptionT[F, LevelInfo],
  withClosesExperiencesResponse: () => OptionT[F, LevelInfo],
) extends FindLevelInfo[F]:
  override def withId(id: Long): OptionT[F, LevelInfo] = withIdResponse.apply()

  override def closestWithExperienceLowerThan(exp: BigInt): OptionT[F, LevelInfo] =
    withClosesExperiencesResponse.apply()
