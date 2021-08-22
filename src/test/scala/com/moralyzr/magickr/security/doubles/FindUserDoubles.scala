package com.moralyzr.magickr.security.doubles

import cats.data.OptionT
import cats.effect.IO
import com.moralyzr.magickr.security.core.models.User
import com.moralyzr.magickr.security.core.ports.outgoing.FindUser
import com.moralyzr.magickr.security.core.types.EmailType.Email
import com.moralyzr.magickr.security.fixtures.UserFixtures

class FindUserOk(userToReturn: () => User) extends FindUser[IO] {
  override def withId(id: Long): OptionT[IO, User] =
    OptionT.pure(userToReturn.apply())

  override def withEmail(email: Email): OptionT[IO, User] =
    OptionT.pure(userToReturn.apply())
}

class FindUserNotFound() extends FindUser[IO] {
  override def withId(id: Long): OptionT[IO, User] = OptionT.none

  override def withEmail(email: Email): OptionT[IO, User] = OptionT.none
}
