package com.moralyzr.magickr.security.adapters.userdata.doobie

import cats.data.OptionT
import cats.effect.IO
import com.moralyzr.magickr.security.core.models.User
import com.moralyzr.magickr.security.core.ports.outgoing.FindUserWithCredentials
import com.moralyzr.magickr.security.core.types.EmailType.Email
import com.moralyzr.magickr.security.core.types.PasswordType.Password
import com.moralyzr.magickr.security.core.types.implicits._
import doobie.free.connection.ConnectionIO
import doobie.util.query.Query0
import doobie.util.transactor.Transactor
import doobie.implicits.toConnectionIOOps
import doobie.implicits.toSqlInterpolator

private object UserSql:
  def findUserWithCredentials(email: String, password: String): Query0[User] =
    sql"""
     SELECT * FROM User WHERE email = $email AND password = $password
  """.query

class UserRepository(val xa: Transactor[IO]) extends FindUserWithCredentials :
  override def withCredentials(email: Email, password: Password): IO[Option[User]] =
    UserSql.findUserWithCredentials(email.toString, password.toString).option.transact(xa)
