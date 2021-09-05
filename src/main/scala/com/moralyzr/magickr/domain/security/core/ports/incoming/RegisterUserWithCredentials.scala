package com.moralyzr.magickr.domain.security.core.ports.incoming

import cats.data.EitherT
import com.moralyzr.magickr.domain.security.core.errors.AuthError
import com.moralyzr.magickr.domain.security.core.models.User
import io.circe.generic.auto.*

import java.time.LocalDate

trait RegisterUserWithCredentials[F[_]]:
  def register(
    command: RegisterUserWithCredentialsCommand
  ): EitherT[F, AuthError, User]

final case class RegisterUserWithCredentialsCommand(
  val name     : String,
  val lastName : String,
  val email    : String,
  val password : String,
  val birthDate: LocalDate
)
