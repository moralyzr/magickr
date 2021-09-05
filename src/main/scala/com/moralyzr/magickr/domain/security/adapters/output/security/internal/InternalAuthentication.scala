package com.moralyzr.magickr.domain.security.adapters.output.security.internal

import cats.data.EitherT
import cats.effect.IO
import cats.Monad
import cats.effect.kernel.Sync
import com.moralyzr.magickr.domain.security.core.errors.AuthError
import com.moralyzr.magickr.domain.security.core.models.User
import com.moralyzr.magickr.domain.security.core.ports.outgoing.{Authentication, JwtManager}
import com.moralyzr.magickr.domain.security.core.validations.PasswordValidationAlgebra
import com.moralyzr.magickr.domain.security.core.ports.outgoing.Authentication
import com.moralyzr.magickr.domain.security.core.types.EmailType.Email
import com.moralyzr.magickr.domain.security.core.types.PasswordType.Password
import com.moralyzr.magickr.domain.security.core.types.TokenType.Token

class InternalAuthentication[F[_] : Sync](
  private val passwordValidation: PasswordValidationAlgebra,
  private val jwtManager        : JwtManager[F]
) extends Authentication[F] :

  override def forUser(
    user    : User,
    password: String
  ): EitherT[F, AuthError, Token] =
    val token = for {
      _ <- passwordValidation.invalidPassword(password, user.password)
      token <- jwtManager.generate(user.email)
    } yield token
    EitherT.fromEither(token)

object InternalAuthentication:
  def apply[F[_] : Sync](
    passwordValidationAlgebra: PasswordValidationAlgebra,
    jwtManager               : JwtManager[F]
  ): InternalAuthentication[F] =
    new InternalAuthentication[F](passwordValidationAlgebra, jwtManager)
