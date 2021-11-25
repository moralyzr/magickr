package com.moralyzr.magickr.domain.adventurer.development.core.ports.outgoing

import cats.data.OptionT
import cats.Monad
import com.moralyzr.magickr.domain.adventurer.development.core.models.AdventurerLevel

trait FindAdventurerLevel[F[_] : Monad]:
  def withId(id: Long): OptionT[F, AdventurerLevel]
  def withAdventurerId(adventurerId: Long): OptionT[F, AdventurerLevel]
