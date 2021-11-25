package com.moralyzr.magickr.domain.adventurer.development.core

import com.moralyzr.magickr.domain.adventurer.development.core.events.{
  AdventurerLeveledDown,
  AdventurerLeveledUp,
  AdventurerLevelingEvent,
}
import com.moralyzr.magickr.domain.adventurer.development.core.models.{AdventurerLevel, LevelInfo}

object CheckAdventurerLevel:

  def ifNeedUpdate(
    adventurerLevel: AdventurerLevel,
    closestLevel: LevelInfo,
  ): (AdventurerLevel, AdventurerLevelingEvent) = adventurerLevel match {
    case _ if adventurerLevel.level > closestLevel.level => levelUp(adventurerLevel, closestLevel)
    case _ if adventurerLevel.level < closestLevel.level => levelDown(adventurerLevel, closestLevel)
    case _ => (adventurerLevel, null)
  }

  private def levelUp(adventurerLevel: AdventurerLevel, closestLevel: LevelInfo) = {
    val newLevel = adventurerLevel.copy(level = closestLevel.level)
    val event = AdventurerLeveledUp(
      adventurerId = adventurerLevel.adventurerId,
      oldLevel = adventurerLevel.level,
      newLevel = closestLevel.level,
    )
    (newLevel, event)
  }

  private def levelDown(adventurerLevel: AdventurerLevel, closestLevel: LevelInfo) = {
    val newLevel = adventurerLevel.copy(level = closestLevel.level)
    val event = AdventurerLeveledDown(
      adventurerId = adventurerLevel.adventurerId,
      oldLevel = adventurerLevel.level,
      newLevel = closestLevel.level,
    )
    (newLevel, event)
  }
