package com.moralyzr.magickr.security.core.ports.outgoing

import cats.data.EitherT
import cats.effect.IO
import com.moralyzr.magickr.security.core.errors.AuthError
import com.moralyzr.magickr.security.core.models.User

trait ValidateAuthToken:
  def token(command: LoginUserByTokenCommand): EitherT[IO, AuthError, User]

case class LoginUserByTokenCommand(
  val token: String,
)
