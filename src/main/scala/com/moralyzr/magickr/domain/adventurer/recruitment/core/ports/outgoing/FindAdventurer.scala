package com.moralyzr.magickr.domain.adventurer.recruitment.core.ports.outgoing

import cats.data.OptionT
import com.moralyzr.magickr.domain.adventurer.recruitment.core.models.Adventurer

trait FindAdventurer[F[_]]:
  def withId(id: Long): OptionT[F, Adventurer]

  def forUser(id: Long): OptionT[F, Adventurer]
