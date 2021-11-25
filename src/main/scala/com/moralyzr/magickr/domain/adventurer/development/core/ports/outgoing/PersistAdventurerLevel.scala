package com.moralyzr.magickr.domain.adventurer.development.core.ports.outgoing

import com.moralyzr.magickr.domain.adventurer.development.core.models.AdventurerLevel

trait PersistAdventurerLevel[F[_]]:
  def save(adventurerLevel: AdventurerLevel): F[AdventurerLevel]
