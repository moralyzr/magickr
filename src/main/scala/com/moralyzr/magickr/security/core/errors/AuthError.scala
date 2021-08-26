package com.moralyzr.magickr.security.core.errors

import com.moralyzr.magickr.infrastructure.errorhandling.Problem

sealed trait AuthError extends Problem

case class InvalidToken(
  url: String = "/problem/auth/invalid-token",
  code: String = "AUTH_INVALID_TOKEN",
  title: String = "Invalid Token",
  message: String = "The informed authentication token is invalid"
)

case class InvalidCredentials(
  url: String = "/problem/auth/invalid-credentials",
  code: String = "AUTH_INVALID_CREDENTIALS",
  title: String = "Invalid Credentials",
  message: String = "The informed user credentials are invalid!"
) extends AuthError

case class UserNotFound(
  url: String = "/problem/auth/user-not-found",
  code: String = "AUTH_USER_NOT_FOUND",
  title: String = "Invalid Credentials",
  message: String = "The informed user does not exists!"
) extends AuthError

case class UserAlreadyExists(
  url: String = "/problem/auth/user-already-exists",
  code: String = "AUTH_USER_ALREADY_EXISTS",
  title: String = "User Already Exists",
  message: String = "The informed user is already registered!"
) extends AuthError
