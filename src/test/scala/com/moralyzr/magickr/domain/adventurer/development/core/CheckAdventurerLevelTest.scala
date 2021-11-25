package com.moralyzr.magickr.domain.adventurer.development.core

import com.moralyzr.magickr.domain.adventurer.development.core.models.{AdventurerLevel, LevelInfo}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class CheckAdventurerLevelTest extends AnyFlatSpec {
  behavior of "The adventurer level checker"

  it should "return an updated adventurer and level up event if the closest level is higher" in {
    val adventurerLevel = AdventurerLevel(id = 1L, adventurerId = 1L, level = 1, currentExperience = 1200)
    val closedLevel = LevelInfo(id = Some(1L), level = 2, fromExperience = 1200)

    val (updatedLevel, event) = CheckAdventurerLevel.ifNeedUpdate(adventurerLevel, closedLevel)

    updatedLevel.level shouldBe 2
    event.adventurerId shouldBe 1L
    event.newLevel shouldBe 2
    event.oldLevel shouldBe 1
  }

  it should "return an updated adventurer and level down event if the closest level is lower" in {
    val adventurerLevel = AdventurerLevel(id = 1L, adventurerId = 1L, level = 2, currentExperience = 800)
    val closedLevel = LevelInfo(id = Some(1L), level = 1, fromExperience = 500)

    val (updatedLevel, event) = CheckAdventurerLevel.ifNeedUpdate(adventurerLevel, closedLevel)

    updatedLevel.level shouldBe 1
    event.adventurerId shouldBe 1L
    event.newLevel shouldBe 1
    event.oldLevel shouldBe 2
  }

  it should "return just an adventurer, if no lvl changed" in {
    val adventurerLevel = AdventurerLevel(id = 1L, adventurerId = 1L, level = 2, currentExperience = 1600)
    val closedLevel = LevelInfo(id = Some(1L), level = 2, fromExperience = 1500)

    val (updatedLevel, event) = CheckAdventurerLevel.ifNeedUpdate(adventurerLevel, closedLevel)

    updatedLevel.level shouldBe 2
    event shouldBe null
  }

}
