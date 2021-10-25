package com.moralyzr.magickr.domain.adventurer.adapters.output.databases.sql

import cats.effect.unsafe.implicits.global
import com.moralyzr.magickr.helpers.database.DatabaseTestConnectionHelper
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import cats.effect.IO
import com.moralyzr.magickr.domain.adventurer.fixtures.AdventurerFixture
import com.moralyzr.magickr.domain.security.adapters.output.databases.postgres.UserRepository
import com.moralyzr.magickr.domain.security.fixtures.UserFixtures
import cats.effect.kernel.Resource

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
    val existingUser = UserFixtures.user
    val expectedAdventurer = AdventurerFixture.adventurer

    val theAdventurer = (for {
      tx <- databaseHelper
      userRepository = UserRepository[IO](tx)
      adventurerRepository = AdventurerRepository[IO](tx)
      savedUser <- Resource.eval(userRepository.save(existingUser))
      userId = savedUser.id.getOrElse(-1L)
      savedAdventurer <- Resource.eval(adventurerRepository.save(expectedAdventurer.copy(userId = userId)))
      savedAdventurerId = savedAdventurer.id.getOrElse(-1L)
      foundAdventurer = adventurerRepository.withId(savedAdventurerId)
    } yield foundAdventurer).use(_.value).unsafeRunSync()

    theAdventurer.isEmpty shouldBe false
  }

  it should "an existing adventurer should be fetched by the UserId" in {
    val existingUser = UserFixtures.user
    val expectedAdventurer = AdventurerFixture.adventurer

    val theAdventurer = (for {
      tx <- databaseHelper
      userRepository = UserRepository[IO](tx)
      adventurerRepository = AdventurerRepository[IO](tx)
      savedUser <- Resource.eval(userRepository.save(existingUser))
      userId = savedUser.id.getOrElse(-1L)
      savedAdventurer <- Resource.eval(adventurerRepository.save(expectedAdventurer.copy(userId = userId)))
      foundAdventurer = adventurerRepository.forUser(userId)
    } yield foundAdventurer).use(_.value).unsafeRunSync()

    theAdventurer.isEmpty shouldBe false
  }

}
