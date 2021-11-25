package com.moralyzr.magickr.domain.adventurer.recruitment.core.mappers

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import com.moralyzr.magickr.domain.adventurer.recruitment.RecruitmentArbitraries.recruitNewAdventurerCommand
import com.moralyzr.magickr.domain.adventurer.recruitment.RecruitmentArbitraries.arbitraryLongs
import com.moralyzr.magickr.domain.adventurer.recruitment.core.commands.RecruitNewAdventurerCommand
import com.moralyzr.magickr.domain.adventurer.recruitment.core.models.Adventurer
import org.scalacheck.Prop.forAll
import org.scalatestplus.scalacheck.{Checkers, ScalaCheckDrivenPropertyChecks}
import org.scalatest.flatspec.AnyFlatSpec

import scala.language.postfixOps

class AdventurerMapperTypeTest extends AnyFlatSpec with Matchers with ScalaCheckDrivenPropertyChecks {
  behavior of "An Adventurer Mapper"

  it should "map from a RecruitmentCommand Successfully" in {
    forAll(arbitraryLongs, recruitNewAdventurerCommand) { (userId, arbitraryCommand) =>
      arbitraryCommand.arbitrary.sample.map { command =>
        AdventurerMapper.fromRecruitCommand(userId, command) shouldBe Adventurer(
          userId = userId,
          avatar = command.avatar.getOrElse("/img/avatar/default.png"),
          name = command.name,
          title = command.title,
        )
      }
    }
  }

}
