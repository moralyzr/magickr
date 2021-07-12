package com.moralyzr.magickr.security.core.types

object EmailType:
  opaque type Email = String

  def fromString(email: String): Email = email
