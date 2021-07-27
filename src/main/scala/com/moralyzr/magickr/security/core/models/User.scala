package com.moralyzr.magickr.security.core.models

import com.moralyzr.magickr.security.core.types.EmailType.Email
import com.moralyzr.magickr.security.core.types.PasswordType.Password

import java.time.LocalDate

case class User(
  val id: Long,
  val name: String,
  val lastName: String,
  val email: Email,
  val password: Password,
  val active: Boolean,
  val birthDate: LocalDate,
)
