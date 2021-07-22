package com.moralyzr.magickr.security.core.ports.outgoing

import cats.data.OptionT
import cats.effect.IO
import com.moralyzr.magickr.security.core.models.User
import com.moralyzr.magickr.security.core.types.EmailType.Email
import com.moralyzr.magickr.security.core.types.PasswordType.Password

trait FindUser[F[_]]:
  def withEmail(email: Email): OptionT[F, User]
