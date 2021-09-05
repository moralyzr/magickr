package com.moralyzr.magickr.domain.security.core.interpreters

import cats.data.EitherT
import cats.Monad
import cats.implicits.*
import com.moralyzr.magickr.domain.security.core.errors.{AuthError, UserAlreadyExists, UserNotFound}
import com.moralyzr.magickr.domain.security.core.ports.outgoing.FindUser
import com.moralyzr.magickr.domain.security.core.types.EmailType
import com.moralyzr.magickr.domain.security.core.validations.UserValidationAlgebra
import com.moralyzr.magickr.domain.security.core.errors.UserAlreadyExists

class UserValidationInterpreter[F[_] : Monad](private val findUser: FindUser[F]) extends UserValidationAlgebra[F] :

  override def shouldExist(userId: Long): EitherT[F, AuthError, Unit] =
    findUser
      .withId(userId)
      .toRight(new UserNotFound())
      .void

  override def shouldExist(email: String): EitherT[F, AuthError, Unit] =
    findUser
      .withEmail(EmailType.fromString(email))
      .toRight(new UserNotFound())
      .void

  override def doesNotExist(userId: Long): EitherT[F, AuthError, Unit] =
    findUser
      .withId(userId)
      .map(_ => new UserAlreadyExists())
      .toLeft(())

  override def doesNotExist(email: String): EitherT[F, AuthError, Unit] =
    findUser
      .withEmail(EmailType.fromString(email))
      .map(_ => new UserAlreadyExists())
      .toLeft(())

object UserValidationInterpreter:
  def apply[F[_] : Monad](findUser: FindUser[F]): UserValidationInterpreter[F] =
    new UserValidationInterpreter[F](findUser)
