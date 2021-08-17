package com.moralyzr.magickr.security.adapters.output.databases.postgres

import cats.data.OptionT
import cats.effect.kernel.{Async, Resource}
import com.moralyzr.magickr.security.core.models.User
import com.moralyzr.magickr.security.core.ports.outgoing.FindUser
import com.moralyzr.magickr.security.core.types.EmailType.Email
import com.moralyzr.magickr.security.core.types.PasswordType.Password
import com.moralyzr.magickr.security.core.types.implicits.*
import doobie.free.connection.ConnectionIO
import doobie.implicits.{toConnectionIOOps, toSqlInterpolator}
import doobie.postgres.implicits.*
import doobie.util.query.Query0
import doobie.util.transactor.Transactor
import com.moralyzr.magickr.security.core.ports.outgoing.PersistUser
import cats.data.EitherT
import com.moralyzr.magickr.infrastructure.errorhandling.Problem
import doobie.util.update.Update0
import com.moralyzr.magickr.infrastructure.database.doobie.implicits
import com.moralyzr.magickr.infrastructure.database.doobie.implicits
import com.moralyzr.magickr.infrastructure.database.doobie.implicits.SqlLogHandler
import doobie.util.log.LogHandler
import cats.implicits.*
import com.moralyzr.magickr.security.core.errors.AuthError

private object UserSql:
  def findWithId(id: Long): Query0[User] =
    sql"""
       SELECT * FROM Users WHERE id = $id
       """.query

  def findUserWithEmail(email: Email): Query0[User] =
    sql"""
     SELECT * FROM Users WHERE email = $email
       """.query

  def saveUser(user: User): Update0 = sql"""
     INSERT INTO Users (name, lastName, email, password, active, birthDate)
     VALUES (${user.name} , ${user.lastName}, ${user.email}, ${user.password}, false, ${user.birthDate})
    """.update

  def updateUser(user: User): Update0 = sql"""
     UPDATE Users SET
        name = ${user.name},
        lastName = ${user.lastName},
        email = ${user.email},
        password = ${user.password},
        active = ${user.active},
        birthDate = ${user.birthDate}
      WHERE
        id = ${user.id}
  """.update

class UserRepository[F[_]: Async](val xa: Transactor[F])
    extends FindUser[F]
    with PersistUser[F]:

  override def withId(id: Long): OptionT[F, User] =
    OptionT(UserSql.findWithId(id).option.transact(xa))

  override def withEmail(email: Email): OptionT[F, User] =
    OptionT(UserSql.findUserWithEmail(email).option.transact(xa))

  override def save(user: User): F[User] =
    UserSql
      .saveUser(user = user)
      .withUniqueGeneratedKeys[Long]("id")
      .map(id => user.copy(Some(id)))
      .transact(xa)

  override def update(user: User): OptionT[F, User] = OptionT(for {
    result <- UserSql.updateUser(user).run.transact(xa)
    updatedUser <- result match {
      case 1 => user.id.fold(OptionT.none.value)(withId(_).value)
      case _ => OptionT.none.value
    }
  } yield (updatedUser))

object UserRepository:
  def apply[F[_]: Async](xa: Transactor[F]) = new UserRepository[F](xa)
