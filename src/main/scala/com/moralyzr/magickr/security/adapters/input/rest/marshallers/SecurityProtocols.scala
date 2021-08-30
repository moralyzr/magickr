package com.moralyzr.magickr.security.adapters.input.rest.marshallers

import cats.effect.kernel.Async
import com.moralyzr.magickr.security.core.errors.{AuthError, InvalidCredentials, InvalidToken, UserAlreadyExists, UserNotFound}
import com.moralyzr.magickr.security.core.models.User
import com.moralyzr.magickr.security.core.ports.incoming.{LoginUserByCredentialsCommand, RegisterUserWithCredentialsCommand}
import com.moralyzr.magickr.security.core.types.{EmailType, PasswordType, TokenType}
import com.moralyzr.magickr.security.core.types.EmailType.Email
import com.moralyzr.magickr.security.core.types.PasswordType.Password
import com.moralyzr.magickr.security.core.types.TokenType.*
import io.circe.{Decoder, Encoder}
import org.http4s.EntityDecoder
import org.http4s.circe.CirceEntityCodec.*
import org.http4s.circe.*

trait SecurityProtocols[F[_] : Async]:

  import io.circe.*
  import io.circe.generic.semiauto.*

  given emailDecoder: Decoder[Email] = Decoder.decodeString.map(s => EmailType.fromString(s))

  given emailEncoder: Encoder[Email] = Encoder.encodeString.contramap(s => s.toString)

  given tokenDecoder: Decoder[Token] = Decoder.decodeString.map(token => fromString(token))

  given tokenEncoder: Encoder[Token] = Encoder.encodeString.contramap(s => s.toString)

  given passwordDecoder: Decoder[Password] = Decoder.decodeString.map(s => PasswordType.fromString(s))

  given passwordEncoder: Encoder[Password] = Encoder.encodeString.contramap(s => s.toString)

  given userAlreadyExistsEncoder: Encoder[UserAlreadyExists] = deriveEncoder

  given userNotFoundEncoder: Encoder[UserNotFound] = deriveEncoder

  given invalidCredentialsEncoder: Encoder[InvalidCredentials] = deriveEncoder

  given invalidTokenEncoder: Encoder[InvalidToken] = deriveEncoder

  given authErrorEncoder: Encoder[AuthError] = Encoder.instance {
    case invalidToken@InvalidToken(_, _, _, _)             => invalidTokenEncoder.apply(invalidToken)
    case invalidCredentials@InvalidCredentials(_, _, _, _) => invalidCredentialsEncoder.apply(invalidCredentials)
    case userNotFound@UserNotFound(_, _, _, _)             => userNotFoundEncoder.apply(userNotFound)
    case userAlreadyExists@UserAlreadyExists(_, _, _, _)   => userAlreadyExistsEncoder.apply(userAlreadyExists)
  }

  given loginUserByCredentialsEntityDecoder: EntityDecoder[F, LoginUserByCredentialsCommand] = jsonOf[F,
    LoginUserByCredentialsCommand]

  given loginUserByCredentialsCommandDecoder: Decoder[LoginUserByCredentialsCommand] = deriveDecoder

  given loginUserByCredentialsCommandEncoder: Encoder[LoginUserByCredentialsCommand] = deriveEncoder

  given registerUserWithCredentialsCommandDecoder: Decoder[RegisterUserWithCredentialsCommand] = deriveDecoder

  given registerUserWithCredentialsCommandEncoder: Encoder[RegisterUserWithCredentialsCommand] = deriveEncoder

  given userDecoder: Decoder[User] = deriveDecoder

  given userEncoder: Encoder[User] = deriveEncoder
