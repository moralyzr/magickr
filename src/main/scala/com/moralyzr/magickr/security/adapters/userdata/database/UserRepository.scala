package com.moralyzr.magickr.security.adapters.userdata.database

import cats.data.OptionT
import cats.effect.IO
import cats.effect.kernel.Resource
import com.moralyzr.magickr.security.core.models.User
import com.moralyzr.magickr.security.core.ports.outgoing.FindUser
import com.moralyzr.magickr.security.core.types.EmailType.Email
import com.moralyzr.magickr.security.core.types.PasswordType.Password
import com.moralyzr.magickr.security.core.types.implicits.*
import doobie.postgres.implicits.*
import doobie.free.connection.ConnectionIO
import doobie.implicits.{toConnectionIOOps, toSqlInterpolator}
import doobie.util.query.Query0
import doobie.util.transactor.Transactor

private object UserSql:
  def findUserWithEmail(email: Email): Query0[User] =
    sql"""
     SELECT * FROM Users WHERE email = $email
       """.query

class UserRepository(val xa: Transactor[IO]) extends FindUser :
  override def withEmail(email: Email): OptionT[IO, User] =
    OptionT(UserSql.findUserWithEmail(email).option.transact(xa))

object UserRepository:
  def apply(xa: Transactor[IO]): Resource[IO, UserRepository] =
    Resource.make(IO(new UserRepository(xa)))(_ => IO.unit)
