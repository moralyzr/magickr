package com.moralyzr.magickr.domain.security.core.models

import com.moralyzr.magickr.domain.security.core.types.EmailType.Email
import com.moralyzr.magickr.domain.security.core.types.PasswordType.Password

import java.time.LocalDate

case class User(
  val id       : Option[Long] = None,
  val name     : String,
  val lastName : String,
  val email    : Email,
  val password : Password,
  val active   : Boolean,
  val birthDate: LocalDate
)
