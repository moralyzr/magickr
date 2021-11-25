package com.moralyzr.magickr.domain.security.core.errors

import com.moralyzr.magickr.infrastructure.errorhandling.Problem

import java.net.HttpURLConnection

sealed trait AuthError extends Problem

case class InvalidToken() extends AuthError:
  override val url: String = "/problem/auth/invalid-token"
  override val code: String = "AUTH_INVALID_TOKEN"
  override val title: String = "Invalid Token"
  override val message: String = "The informed authentication token is invalid"

case class InvalidCredentials() extends AuthError {
  override val url: String = "/problem/auth/invalid-credentials"
  override val code: String = "AUTH_INVALID_CREDENTIALS"
  override val title: String = "Invalid Credentials"
  override val message: String = "The informed user credentials are invalid!"
}

case class UserNotFound() extends AuthError:
  override val url: String = "/problem/auth/user-not-found"
  override val code: String = "AUTH_USER_NOT_FOUND"
  override val title: String = "Invalid Credentials"
  override val message: String = "The informed user does not exists!"

case class UserAlreadyExists() extends AuthError:
  override val url: String = "/problem/auth/user-already-exists"
  override val code: String = "AUTH_USER_ALREADY_EXISTS"
  override val title: String = "User Already Exists"
  override val message: String = "The informed user is already registered!"
