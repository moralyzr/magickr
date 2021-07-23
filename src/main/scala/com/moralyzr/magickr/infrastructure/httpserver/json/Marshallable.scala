package com.moralyzr.magickr.infrastructure.httpserver.json

import akka.http.scaladsl.marshalling.ToResponseMarshaller
import cats.effect.IO

trait Marshallable[F[_]]:
  def marshaller[A: ToResponseMarshaller]: ToResponseMarshaller[F[A]]

object Marshallable:
  implicit def marshaller[F[_], A : ToResponseMarshaller](implicit M: Marshallable[F]): ToResponseMarshaller[F[A]] =
    M.marshaller

  given ioMarshaller: Marshallable[IO] with
    def marshaller[A: ToResponseMarshaller] = implicitly
