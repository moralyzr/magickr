package com.moralyzr.magickr.security.core.errors

import com.moralyzr.magickr.infrastructure.errorhandling.Problem

sealed class AuthError(url: String, code: String, title: String, message: String)
  extends Problem(url = url, code = code, title = title, message = message)

case class InvalidToken() extends AuthError(
  url = "/problem/auth/invalid-token",
  code = "AUTH_INVALID_TOKEN",
  title = "Invalid Token",
  message = "The informed authentication token is invalid",
)
