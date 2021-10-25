package com.moralyzr.magickr.domain.security.adapters.input.rest

import cats.data.EitherT
import cats.effect.kernel.Async
import cats.implicits.*
import com.moralyzr.magickr.domain.security.adapters.input.rest.marshallers.SecurityProtocols
import com.moralyzr.magickr.domain.security.core.types.TokenType.Token
import com.moralyzr.magickr.domain.security.core.SecurityManagement
import com.moralyzr.magickr.domain.security.core.errors.{AuthError, InvalidCredentials, InvalidToken,
  UserAlreadyExists, UserNotFound}
import com.moralyzr.magickr.domain.security.core.models.User
import com.moralyzr.magickr.domain.security.core.ports.incoming.{LoginUserByCredentials,
  LoginUserByCredentialsCommand, RegisterUserWithCredentials, RegisterUserWithCredentialsCommand}
import com.moralyzr.magickr.domain.security.core.types.EmailType
import io.circe.generic.auto.*
import org.http4s.dsl.Http4sDsl
import org.http4s.{EntityDecoder, HttpRoutes, Status}
import org.http4s.circe.CirceEntityCodec.*

class SecurityApi[F[_] : Async](
  private val securityManagement: SecurityManagement[F]
) extends LoginUserByCredentials[F]
  with Http4sDsl[F]
  with RegisterUserWithCredentials[F]
  with SecurityProtocols[F] :

  private def loginRoute: HttpRoutes[F] = HttpRoutes.of[F] {
    case req@POST -> Root / "login" =>
      val result = for {
        command <- req.as[LoginUserByCredentialsCommand]
        loginResult <- login(command).value
      } yield loginResult

      result.flatMap {
        case Right(token)                                 => Ok(token)
        case Left(userNotFound: UserNotFound)             => NotFound(userNotFound)
        case Left(invalidCredentials: InvalidCredentials) => BadRequest(invalidCredentials)
        case Left(err: AuthError)                         => InternalServerError(err)
      }
  }

  private def registerWithCredentialsRoute: HttpRoutes[F] = HttpRoutes.of[F] {
    case req@POST -> Root / "register" =>
      val result = for {
        command <- req.as[RegisterUserWithCredentialsCommand]
        registerResult <- register(command).value
      } yield registerResult

      result.flatMap {
        case Right(user)                                => Ok(user)
        case Left(userAlreadyExists: UserAlreadyExists) => Conflict(userAlreadyExists)
        case Left(err: AuthError)                       => InternalServerError(err)
      }
  }

  private def endpoints: HttpRoutes[F] =
    loginRoute <+> registerWithCredentialsRoute

  override def login(command: LoginUserByCredentialsCommand): EitherT[F, AuthError, Token] =
    securityManagement.loginWithCredentials(command)

  override def register(command: RegisterUserWithCredentialsCommand): EitherT[F, AuthError, User] =
    securityManagement.registerUserWithCredentials(command)

object SecurityApi:
  def endpoints[F[_] : Async](securityManagement: SecurityManagement[F]) =
    new SecurityApi[F](securityManagement).endpoints

