package com.moralyzr.magickr.security.adapters.input.rest

import com.moralyzr.magickr.security.core.ports.incoming.LoginUserByCredentials
import cats.data.EitherT
import cats.effect.IO
import com.moralyzr.magickr.security.core.ports.incoming.LoginUserByCredentialsCommand
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.server.Route
import io.circe.generic.auto.*
import com.moralyzr.magickr.infrastructure.httpserver.json.Marshallable
import com.moralyzr.magickr.infrastructure.httpserver.json.Marshallable.*
import cats.effect.kernel.Async
import com.moralyzr.magickr.security.adapters.input.rest.marshallers.SecurityProtocols
import com.moralyzr.magickr.security.core.errors.AuthError
import com.moralyzr.magickr.security.core.types.TokenType.Token
import com.moralyzr.magickr.security.core.ports.outgoing.Authentication
import com.moralyzr.magickr.security.core.SecurityManagement
import cats.effect.kernel.Sync

class SecurityApi[F[_]: Async](
    private val securityManagement: SecurityManagement[F]
)(using
    marshaller: Marshallable[F]
) extends LoginUserByCredentials[F]
    with SecurityProtocols:

  def routes: Route =
    (path("/security/auth") & post) {
      entity(as[LoginUserByCredentialsCommand]) {
        (command: LoginUserByCredentialsCommand) =>
          complete {
            login(command)
          }
      }
    }

  override def login(
      command: LoginUserByCredentialsCommand
  ): F[Either[AuthError, Token]] =
    securityManagement.loginWithCredentials(command = command).value

object SecurityApi:
  def apply[F[_]: Async](securityManagement: SecurityManagement[F])(using
      marshaller: Marshallable[F]
  ) = Async[F].delay {
    new SecurityApi[F](securityManagement)
  }