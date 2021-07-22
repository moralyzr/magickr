package com.moralyzr.magickr.security.adapters.output.databases.postgres

import cats.data.OptionT
import cats.effect.kernel.{Resource, Async}
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

private object UserSql:
  def findUserWithEmail(email: Email): Query0[User] =
    sql"""
     SELECT * FROM Users WHERE email = $email
       """.query

class UserRepository[F[_] : Async](val xa: Transactor[F]) extends FindUser[F] :
  override def withEmail(email: Email): OptionT[F, User] =
    OptionT(UserSql.findUserWithEmail(email).option.transact(xa))

object UserRepository:
  def apply[F[_] : Async](xa: Transactor[F]) = new UserRepository[F](xa)
