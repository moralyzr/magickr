package com.moralyzr.magickr.security.adapters.input.rest.marshallers

import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport
import com.moralyzr.magickr.security.core.ports.incoming.LoginUserByCredentialsCommand
import com.moralyzr.magickr.security.core.types.EmailType.Email
import io.circe.Decoder
import io.circe.Encoder
import com.moralyzr.magickr.security.core.types.EmailType
import com.moralyzr.magickr.security.core.types.TokenType
import com.moralyzr.magickr.security.core.errors.AuthError

trait SecurityProtocols extends ErrorAccumulatingCirceSupport:
  import io.circe.generic.semiauto._

  given emailDecoder: Decoder[Email] = Decoder.decodeString.map(s => EmailType.fromString(s))
  given emailEncoder: Encoder[Email] = Encoder.encodeString.contramap(s => s.toString)
  given tokenDecoder: Decoder[TokenType.Token] = Decoder.decodeString.map(token => TokenType.fromString(token))
  given tokenEncoder: Encoder[TokenType.Token] = Encoder.encodeString.contramap(s => s.toString)
  given authErrorDecoder: Decoder[AuthError] = deriveDecoder
  given authErrorEncoder: Encoder[AuthError] = deriveEncoder
  given loginUserByCredentialsCommandDecoder: Decoder[LoginUserByCredentialsCommand] = deriveDecoder
  given loginUserByCredentialsCommandEncoder: Encoder[LoginUserByCredentialsCommand] = deriveEncoder
