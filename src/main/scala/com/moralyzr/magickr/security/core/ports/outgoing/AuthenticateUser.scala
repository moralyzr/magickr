package com.moralyzr.magickr.security.core.ports.outgoing

import cats.data.EitherT
import cats.effect.IO
import com.moralyzr.magickr.security.core.errors.AuthError
import com.moralyzr.magickr.security.core.models.User
import com.moralyzr.magickr.security.core.types.EmailType.Email
import com.moralyzr.magickr.security.core.types.PasswordType.Password
import com.moralyzr.magickr.security.core.types.TokenType.Token

trait AuthenticateUser:
  def withCredentials(email: Email, password: String): EitherT[IO, AuthError, User]
