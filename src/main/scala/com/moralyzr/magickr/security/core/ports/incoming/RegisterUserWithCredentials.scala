package com.moralyzr.magickr.security.core.ports.incoming

import com.moralyzr.magickr.security.core.errors.AuthError
import com.moralyzr.magickr.security.core.models.User

import java.time.LocalDate

trait RegisterUserWithCredentials[F[_]]:
  def register(
    command: RegisterUserWithCredentialsCommand
  ): F[Either[AuthError, User]]

final case class RegisterUserWithCredentialsCommand(
  val name: String,
  val lastName: String,
  val email: String,
  val password: String,
  val birthDate: LocalDate
)
