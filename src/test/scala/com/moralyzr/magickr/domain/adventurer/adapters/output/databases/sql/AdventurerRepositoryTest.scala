package com.moralyzr.magickr.domain.adventurer.adapters.output.databases.sql

import cats.effect.unsafe.implicits.global
import com.moralyzr.magickr.helpers.database.DatabaseTestConnectionHelper
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import cats.effect.IO
import com.moralyzr.magickr.domain.adventurer.fixtures.AdventurerFixture
import com.moralyzr.magickr.domain.security.adapters.output.databases.postgres.UserRepository
import com.moralyzr.magickr.domain.security.fixtures.UserFixtures

class AdventurerRepositoryTest extends AnyFlatSpec
                               with Matchers {
  behavior of "An AdventurerRepository Repository"

  val databaseHelper = DatabaseTestConnectionHelper.buildTransactor()

  it should "fetch an adventurer from the database, and if it does not exists, returns empty optional" in {
    val queryResult = databaseHelper
      .use { tx =>
        val adventurerRepository = AdventurerRepository[IO](tx)
        val result               = adventurerRepository.withId(1L)
        result.value
      }
      .unsafeRunSync()

    queryResult.isEmpty shouldBe true
  }

  it should "fetch an adventurer by userId from the database, and if it does not exists, returns empty optional" in {
    val queryResult = databaseHelper
      .use { tx =>
        val adventurerRepository = AdventurerRepository[IO](tx)
        val result               = adventurerRepository.forUser(1L)
        result.value
      }
      .unsafeRunSync()

    queryResult.isEmpty shouldBe true
  }

  it should "an existing adventurer should be fetched" in {
    val queryResult = databaseHelper
      .use { tx =>
        val adventurerRepository = AdventurerRepository[IO](tx)
        val userRepository       = UserRepository[IO](tx)
        val createdUser          = userRepository.save(UserFixtures.user)
        val createdAdventurer    = adventurerRepository.save(AdventurerFixture.adventurer)
        val result               = adventurerRepository.withId(1L)
        result.value
      }
      .unsafeRunSync()

    queryResult.isEmpty shouldBe true
  }

  it should "an existing adventurer should be fetched by the UserId" in {
    val queryResult = databaseHelper
      .use { tx =>
        val adventurerRepository = AdventurerRepository[IO](tx)
        val userRepository       = UserRepository[IO](tx)
        val createdUser          = userRepository.save(UserFixtures.user)
        val createdAdventurer    = adventurerRepository.save(AdventurerFixture.adventurer)
        val result               = adventurerRepository.forUser(1L)
        result.value
      }
      .unsafeRunSync()

    queryResult.isEmpty shouldBe true
  }

}
