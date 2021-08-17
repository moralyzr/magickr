package com.moralyzr.magickr.security.core.interpreters

import cats.data.EitherT
import cats.Monad
import com.moralyzr.magickr.security.core.errors.AuthError
import com.moralyzr.magickr.security.core.ports.outgoing.FindUser
import com.moralyzr.magickr.security.core.validations.UserValidationAlgebra

class UserValidationInterpreter[F[_]: Monad](private val findUser: FindUser[F])
    extends UserValidationAlgebra[F]:
  override def exists(userId: Long): EitherT[F, AuthError, Unit] = ???

  override def exists(email: String): EitherT[F, AuthError, Unit] = ???

object UserValidationInterpreter:
  def apply[F[_]: Monad](findUser: FindUser[F]): UserValidationInterpreter[F] =
    new UserValidationInterpreter[F](findUser)
