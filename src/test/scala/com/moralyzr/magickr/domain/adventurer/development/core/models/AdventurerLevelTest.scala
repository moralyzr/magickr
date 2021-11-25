package com.moralyzr.magickr.domain.adventurer.development.core.models

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class AdventurerLevelTest extends AnyFlatSpec {
  behavior of "An adventurer level awarding"

  it should "update an experience with positive values" in {
    val adventurerLevel = AdventurerLevel(id = 1L, adventurerId = 1L, level = 1, currentExperience = 1000)
    val updatedAdventurer = adventurerLevel.awardExperience(1000)

    updatedAdventurer.currentExperience shouldBe 2000
  }

  it should "update an experience with negative values" in {
    val adventurerLevel = AdventurerLevel(id = 1L, adventurerId = 1L, level = 1, currentExperience = 1000)
    val updatedAdventurer = adventurerLevel.awardExperience(500)

    updatedAdventurer.currentExperience shouldBe 1500
  }

  it should "the awarded negative experience should not go lower than 0" in {
    val adventurerLevel = AdventurerLevel(id = 1L, adventurerId = 1L, level = 1, currentExperience = 1000)
    val updatedAdventurer = adventurerLevel.awardExperience(-1500)

    updatedAdventurer.currentExperience shouldBe 0
  }

}
