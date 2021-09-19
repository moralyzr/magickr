package com.moralyzr.magickr.domain.security.core.ports.outgoing

import cats.data.OptionT
import com.moralyzr.magickr.domain.security.core.models.User
import com.moralyzr.magickr.domain.security.core.types.EmailType.Email
import com.moralyzr.magickr.domain.security.core.types.PasswordType.Password

trait FindUser[F[_]]:
  def withId(id: Long): OptionT[F, User]

  def withEmail(email: Email): OptionT[F, User]
