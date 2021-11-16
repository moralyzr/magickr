package com.moralyzr.magickr.domain.adventurer.recruitment.fixtures

import com.moralyzr.magickr.domain.adventurer.recruitment.core.models.Adventurer
import faker.Faker

object AdventurerFixture:

  def adventurer =
    new Adventurer(userId = 1L, avatar = "test/test.png", name = Faker.en.animalName())
