package com.moralyzr.magickr.security.core.interpreters

import cats.data.EitherT
import cats.Monad
import cats.implicits.*
import com.moralyzr.magickr.security.core.errors.{AuthError, UserAlreadyExists}
import com.moralyzr.magickr.security.core.ports.outgoing.FindUser
import com.moralyzr.magickr.security.core.types.EmailType
import com.moralyzr.magickr.security.core.validations.UserValidationAlgebra

class UserValidationInterpreter[F[_]: Monad](private val findUser: FindUser[F])
    extends UserValidationAlgebra[F]:

  override def exists(userId: Long): EitherT[F, AuthError , Unit] =
    findUser
      .withId(userId)
      .toRight(UserAlreadyExists)
      .void

  override def exists(email: String): EitherT[F, AuthError, Unit] =
    findUser
      .withEmail(EmailType.fromString(email))
      .toRight(UserAlreadyExists)
      .void

object UserValidationInterpreter:
  def apply[F[_]: Monad](findUser: FindUser[F]): UserValidationInterpreter[F] =
    new UserValidationInterpreter[F](findUser)
