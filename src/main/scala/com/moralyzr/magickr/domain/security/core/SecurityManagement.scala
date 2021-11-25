package com.moralyzr.magickr.domain.security.core

import cats.data.EitherT
import cats.effect.implicits.*
import cats.effect.kernel.{Async, Resource}
import cats.implicits.*
import com.moralyzr.magickr.domain.security.core.errors.{AuthError, UserNotFound}
import com.moralyzr.magickr.domain.security.core.mappers.UserMappers
import com.moralyzr.magickr.domain.security.core.models.User
import com.moralyzr.magickr.domain.security.core.ports.incoming.{
  LoginUserByCredentialsCommand,
  RegisterUserWithCredentialsCommand,
}
import com.moralyzr.magickr.domain.security.core.ports.outgoing.{Authentication, FindUser, PersistUser}
import com.moralyzr.magickr.domain.security.core.validations.UserValidationAlgebra
import com.moralyzr.magickr.domain.security.core.errors.UserNotFound
import com.moralyzr.magickr.domain.security.core.ports.incoming.LoginUserByCredentialsCommand
import com.moralyzr.magickr.domain.security.core.ports.outgoing.Authentication
import com.moralyzr.magickr.domain.security.core.types.TokenType.Token

class SecurityManagement[F[_]: Async](
  private val findUser: FindUser[F],
  private val persistUser: PersistUser[F],
  private val authentication: Authentication[F],
  private val userValidationAlgebra: UserValidationAlgebra[F],
):

  def loginWithCredentials(command: LoginUserByCredentialsCommand): EitherT[F, AuthError, Token] = for {
    user <- findUser.withEmail(command.email).toRight(new UserNotFound())
    token <- authentication.forUser(user, command.password)
  } yield token

  def registerUserWithCredentials(command: RegisterUserWithCredentialsCommand): EitherT[F, AuthError, User] =
    for {
      _ <- userValidationAlgebra.doesNotExist(command.email)
      userToRegister = UserMappers.from(command)
      savedUser <- EitherT.liftF(persistUser.save(userToRegister))
    } yield savedUser

object SecurityManagement:

  def apply[F[_]: Async](
    findUser: FindUser[F],
    persistUser: PersistUser[F],
    authentication: Authentication[F],
    userValidationAlgebra: UserValidationAlgebra[F],
  ): SecurityManagement[F] =
    new SecurityManagement[F](findUser, persistUser, authentication, userValidationAlgebra)
