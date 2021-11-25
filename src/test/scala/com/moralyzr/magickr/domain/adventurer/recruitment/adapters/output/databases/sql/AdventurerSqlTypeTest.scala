package com.moralyzr.magickr.domain.adventurer.recruitment.adapters.output.databases.sql

import cats.effect.IO
import com.moralyzr.magickr.helpers.test
import com.moralyzr.magickr.helpers.test.db.database.testTransactor
import doobie.Transactor
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import doobie.scalatest.IOChecker
import com.moralyzr.magickr.domain.adventurer.recruitment.RecruitmentArbitraries.adventurer

class AdventurerSqlTypeTest extends AnyFunSuite with Matchers with IOChecker {
  override def transactor: Transactor[IO] = testTransactor

  test("TypeCheck - Adventurer Queries") {
    adventurer.arbitrary.sample.map { adventurer =>
      check(AdventurerSql.save(adventurer))
      check(AdventurerSql.findWithId(adventurer.id.getOrElse(1L)))
      check(AdventurerSql.findForUser(adventurer.userId))
      check(AdventurerSql.update(adventurer.copy(name = "Woofer")))
    }
  }

}
