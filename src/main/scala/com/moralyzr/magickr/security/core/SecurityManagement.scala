package com.moralyzr.magickr.security.core

import cats.data.EitherT
import cats.effect.kernel.{Async, Resource}
import cats.effect.implicits.*
import cats.implicits.*
import com.moralyzr.magickr.security.core.errors.AuthError
import com.moralyzr.magickr.security.core.mappers.UserMappers
import com.moralyzr.magickr.security.core.models.User
import com.moralyzr.magickr.security.core.ports.incoming.{
  LoginUserByCredentialsCommand,
  RegisterUserWithCredentialsCommand
}
import com.moralyzr.magickr.security.core.ports.outgoing.{
  Authentication,
  FindUser,
  PersistUser
}
import com.moralyzr.magickr.security.core.types.TokenType.Token
import com.moralyzr.magickr.security.core.validations.UserValidationAlgebra

class SecurityManagement[F[_]: Async](
  private val findUser: FindUser[F],
  private val persistUser: PersistUser[F],
  private val authentication: Authentication[F],
  private val userValidationAlgebra: UserValidationAlgebra[F]
):
  def loginWithCredentials(
    command: LoginUserByCredentialsCommand
  ): EitherT[F, AuthError, Token] =
    for {
      user <- findUser.withEmail(command.email).toRight(AuthError.UserNotFound)
      token <- authentication.forUser(user, command.password)
    } yield token

  def registerUserWithCredentials(
    command: RegisterUserWithCredentialsCommand
  ): EitherT[F, AuthError, User] = for {
    _ <- userValidationAlgebra.exists(command.email)
    userToRegister = UserMappers.from(command)
    savedUser <- EitherT.liftF(persistUser.save(userToRegister))
  } yield savedUser

object SecurityManagement:
  def apply[F[_]: Async](
    findUser: FindUser[F],
    persistUser: PersistUser[F],
    authentication: Authentication[F],
    userValidationAlgebra: UserValidationAlgebra[F]
  ): SecurityManagement[F] =
    new SecurityManagement[F](
      findUser,
      persistUser,
      authentication,
      userValidationAlgebra
    )
