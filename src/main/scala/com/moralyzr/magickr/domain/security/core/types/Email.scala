package com.moralyzr.magickr.domain.security.core.types

object EmailType:
  opaque type Email = String

  def fromString(email: String): Email = email
