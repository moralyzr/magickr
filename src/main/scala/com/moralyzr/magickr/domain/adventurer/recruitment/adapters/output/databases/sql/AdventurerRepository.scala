package com.moralyzr.magickr.domain.adventurer.recruitment.adapters.output.databases.sql

import cats.data.OptionT
import cats.effect.kernel.Async
import cats.implicits.*
import com.moralyzr.magickr.domain.adventurer.recruitment.core.models.Adventurer
import com.moralyzr.magickr.domain.adventurer.recruitment.core.ports.outgoing.{FindAdventurer, PersistAdventurer}
import doobie.free.connection.ConnectionIO
import doobie.implicits.{toConnectionIOOps, toSqlInterpolator}
import doobie.postgres.implicits.*
import doobie.util.log.LogHandler
import doobie.util.query.Query0
import doobie.util.transactor.Transactor
import doobie.util.update.Update0

private object AdventurerSql:
  def findWithId(id: Long): Query0[Adventurer] =
    sql"""
       SELECT * FROM Adventurers WHERE id = $id
       """.query

  def findForUser(userId: Long): Query0[Adventurer] =
    sql"""
       SELECT * FROM Adventurers WHERE userId = $userId
       """.query

  def save(adventurer: Adventurer): Update0 = sql"""
     INSERT INTO Adventurers (id, userId, avatar, name, title)
     VALUES (
         ${adventurer.id},
         ${adventurer.userId},
         ${adventurer.avatar},
         ${adventurer.name},
         ${adventurer.title}
     )
                                                 """.update

  def update(adventurer: Adventurer): Update0 = sql"""
     UPDATE Adventurers SET
        avatar = ${adventurer.avatar},
        name   = ${adventurer.name},
        title  = ${adventurer.title}
      WHERE
        id = ${adventurer.id}
                                                   """.update


class AdventurerRepository[F[_] : Async](val xa: Transactor[F]) extends FindAdventurer[F]
                                                                with PersistAdventurer[F] :

  override def withId(id: Long): OptionT[F, Adventurer] = OptionT(
    AdventurerSql.findWithId(id).option.transact(xa)
  )

  override def forUser(id: Long): OptionT[F, Adventurer] = OptionT(
    AdventurerSql.findForUser(id).option.transact(xa)
  )

  override def save(adventurer: Adventurer): F[Adventurer] =
    AdventurerSql
      .save(adventurer)
      .withUniqueGeneratedKeys[Long]("id")
      .map(id => adventurer.copy(Some(id)))
      .transact(xa)

  override def update(adventurer: Adventurer): OptionT[F, Adventurer] = OptionT(
    for {
      result <- AdventurerSql.update(adventurer).run.transact(xa)
      updatedAdventurer <- result match {
        case 1 => adventurer.id.fold(OptionT.none.value)(withId(_).value)
        case _ => OptionT.none.value
      }
    } yield (updatedAdventurer)
  )

object AdventurerRepository:
  def apply[F[_] : Async](xa: Transactor[F]) = new AdventurerRepository[F](xa)
