package com.moralyzr.magickr.domain.adventurer.core.ports.outgoing

import cats.data.OptionT
import com.moralyzr.magickr.domain.adventurer.core.models.Adventurer


trait PersistAdventurer[F[_]]:
  def save(adventurer: Adventurer): F[Adventurer]

  def update(adventurer: Adventurer): OptionT[F, Adventurer]