package com.moralyzr.magickr.security.core.ports.outgoing

import cats.data.OptionT
import cats.effect.IO
import com.moralyzr.magickr.security.core.models.User
import com.moralyzr.magickr.security.core.types.EmailType.Email
import com.moralyzr.magickr.security.core.types.PasswordType.Password

trait FindUserWithCredentials:
  def withCredentials(email: Email, password: Password): IO[Option[User]]
