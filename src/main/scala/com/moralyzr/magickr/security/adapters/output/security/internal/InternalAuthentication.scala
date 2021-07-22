package com.moralyzr.magickr.security.adapters.output.security.internal

import cats.data.EitherT
import cats.effect.IO
import cats.Monad
import com.moralyzr.magickr.security.core.errors.AuthError
import com.moralyzr.magickr.security.core.models.User
import com.moralyzr.magickr.security.core.ports.outgoing.{Authentication, FindUser, JwtManager}
import com.moralyzr.magickr.security.core.types.EmailType.Email
import com.moralyzr.magickr.security.core.types.PasswordType.Password
import com.moralyzr.magickr.security.core.types.TokenType.Token
import com.moralyzr.magickr.security.core.validations.PasswordValidationAlgebra

class InternalAuthentication[F[_]](
  private val passwordValidation: PasswordValidationAlgebra,
  private val jwtManager: JwtManager[F],
) extends Authentication[F] :
  override def forUser(user: User, password: String): Either[AuthError, Token] =
    for {
      _ <- passwordValidation.invalidPassword(password, user.password)
      token <- jwtManager.generate(user.email)
    } yield token

object InternalAuthentication:
  def apply[F[_]](passwordValidationAlgebra: PasswordValidationAlgebra, jwtManager: JwtManager[F]): InternalAuthentication[F] =
    new InternalAuthentication(passwordValidationAlgebra, jwtManager)