package com.moralyzr.magickr.security.core.errors

import com.moralyzr.magickr.infrastructure.errorhandling.Problem

enum AuthError(url: String, code: String, title: String, message: String) extends Problem(url, code, title, message) :
  case InvalidToken extends AuthError(
    url = "/problem/auth/invalid-token",
    code = "AUTH_INVALID_TOKEN",
    title = "Invalid Token",
    message = "The informed authentication token is invalid",
  )
  case InvalidCredentials extends AuthError(
    url = "/problem/auth/invalid-credentials",
    code = "AUTH_INVALID_CREDENTIALS",
    title = "Invalid Credentials",
    message = "The informed user credentials are invalid!",
  )
  case UserNotFound extends AuthError(
    url = "/problem/auth/user-not-found",
    code = "AUTH_USER_NOT_FOUND",
    title = "Invalid Credentials",
    message = "The informed user does not exists!",
  )
