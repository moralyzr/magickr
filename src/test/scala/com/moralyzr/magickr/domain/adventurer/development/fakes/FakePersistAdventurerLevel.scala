package com.moralyzr.magickr.domain.adventurer.development.fakes

import com.moralyzr.magickr.domain.adventurer.development.core.models.AdventurerLevel
import com.moralyzr.magickr.domain.adventurer.development.core.ports.outgoing.PersistAdventurerLevel

class FakePersistAdventurerLevel[F[_]](saveResult: () => F[AdventurerLevel]) extends PersistAdventurerLevel[F]:
  override def save(adventurerLevel: AdventurerLevel): F[AdventurerLevel] = saveResult.apply()
