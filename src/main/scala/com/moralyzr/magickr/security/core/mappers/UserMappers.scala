package com.moralyzr.magickr.security.core.mappers

import cats.Monad
import com.moralyzr.magickr.security.core.models.User
import com.moralyzr.magickr.security.core.ports.incoming.RegisterUserWithCredentialsCommand
import com.moralyzr.magickr.security.core.types.{EmailType, PasswordType}

object UserMappers:
  def from(command: RegisterUserWithCredentialsCommand): User = new User(
    name = command.name,
    lastName = command.lastName,
    email = EmailType.fromString(command.email),
    password = PasswordType.fromString(command.password),
    active = false,
    birthDate = command.birthDate
  )
