package com.moralyzr.magickr.domain.adventurer.fixtures

import com.moralyzr.magickr.domain.adventurer.core.models.Adventurer
import faker.Faker

object AdventurerFixture:
  def adventurer = new Adventurer(
    userId = 1L,
    avatar = "test/test.png",
    name = Faker.en.animalName(),
    level = 1,
    currentExperience = 1,
  )