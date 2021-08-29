package com.moralyzr.magickr.security.adapters.input.rest

import cats.data.EitherT
import cats.effect.kernel.Async
import com.moralyzr.magickr.security.core.ports.incoming.{
  LoginUserByCredentials, LoginUserByCredentialsCommand,
  RegisterUserWithCredentials, RegisterUserWithCredentialsCommand
}
import com.moralyzr.magickr.security.core.SecurityManagement
import com.moralyzr.magickr.security.core.errors.AuthError
import com.moralyzr.magickr.security.core.models.User
import com.moralyzr.magickr.security.core.types.TokenType.Token
import org.http4s.{EntityDecoder, HttpRoutes}
import org.http4s.dsl.Http4sDsl
import com.moralyzr.magickr.security.adapters.input.rest.marshallers.SecurityProtocols
import org.http4s.circe.CirceEntityCodec.*
import io.circe.generic.auto.*
import cats.implicits.*
import com.moralyzr.magickr.security.core.types.EmailType
import org.http4s.Status
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
          testResult <- login(command).value
        } yield testResult

        result.flatMap {
          _.fold(
            authError => InternalServerError(authError),
            success => Ok(success)
          )
        }
    }


  override def login(command: LoginUserByCredentialsCommand): EitherT[F, AuthError, Token] =
    securityManagement.loginWithCredentials(command)

  override def register(command: RegisterUserWithCredentialsCommand): EitherT[F, AuthError, User] =
    securityManagement.registerUserWithCredentials(command)

object SecurityApi:
  def endpoints[F[_] : Async](securityManagement: SecurityManagement[F]) =
    new SecurityApi[F](securityManagement).loginRoute

