package com.moralyzr.magickr.domain.adventurer.development

import com.moralyzr.magickr.domain.adventurer.development.core.models.AdventurerLevel
import com.moralyzr.magickr.domain.adventurer.recruitment.core.models.Adventurer
import org.scalacheck.*
import org.scalacheck.Arbitrary.arbitrary

trait DevelopmentArbitraries {

  implicit val adventurerLevel: Arbitrary[AdventurerLevel] = Arbitrary[AdventurerLevel] {
    for {
      id <- Gen.posNum[Long]
      adventurerId <- Gen.posNum[Long]
      level <- Gen.posNum[Int]
      currentExperience <- Gen.posNum[BigInt]
    } yield AdventurerLevel(id, adventurerId, level, currentExperience)
  }

}
