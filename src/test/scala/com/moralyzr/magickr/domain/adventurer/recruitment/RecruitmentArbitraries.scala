package com.moralyzr.magickr.domain.adventurer.recruitment

import com.moralyzr.magickr.domain.adventurer.development.core.models.AdventurerLevel
import com.moralyzr.magickr.domain.adventurer.recruitment.core.commands.RecruitNewAdventurerCommand
import com.moralyzr.magickr.domain.adventurer.recruitment.core.models.Adventurer
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}

trait RecruitmentArbitraries {

  implicit val arbitraryLongs: Gen[Long] = arbitrary[Long]

  implicit val adventurer: Arbitrary[Adventurer] = Arbitrary[Adventurer] {
    for {
      id <- Gen.posNum[Long]
      userId <- Gen.posNum[Long]
      avatar <- arbitrary[String]
      name <- arbitrary[String]
      title <- arbitrary[String]
    } yield Adventurer(Some(id), userId, avatar, name, Some(title))
  }

  implicit val recruitNewAdventurerCommand: Arbitrary[RecruitNewAdventurerCommand] =
    Arbitrary[RecruitNewAdventurerCommand] {
      for {
        name <- arbitrary[String]
        avatar <- arbitrary[String]
        title <- arbitrary[String]
      } yield RecruitNewAdventurerCommand(name, Some(avatar), Some(title))
    }

}

object RecruitmentArbitraries extends RecruitmentArbitraries
