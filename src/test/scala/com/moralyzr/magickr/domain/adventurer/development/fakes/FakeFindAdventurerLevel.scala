package com.moralyzr.magickr.domain.adventurer.development.fakes

import cats.data.OptionT
import cats.Monad
import com.moralyzr.magickr.domain.adventurer.development.core.models.AdventurerLevel
import com.moralyzr.magickr.domain.adventurer.development.core.ports.outgoing.FindAdventurerLevel

class FakeFindAdventurerLevel[F[_]: Monad](
  val withIdResponse: () => OptionT[F, AdventurerLevel],
  val withAdventurerIdResponse: () => OptionT[F, AdventurerLevel],
) extends FindAdventurerLevel[F]:
  override def withId(id: Long): OptionT[F, AdventurerLevel] = withIdResponse.apply()

  override def withAdventurerId(adventurerId: Long): OptionT[F, AdventurerLevel] =
    withAdventurerIdResponse.apply()
