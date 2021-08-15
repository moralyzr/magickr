package com.moralyzr.magickr.infrastructure.httpserver.json

import akka.http.scaladsl.marshalling.{
  LowPriorityToResponseMarshallerImplicits,
  Marshaller,
  ToResponseMarshaller
}
import cats.effect.IO
import cats.effect.unsafe.implicits.global

trait CatsEffectsMarshallers extends LowPriorityToResponseMarshallerImplicits:
  implicit def ioMarshaller[A](implicit
      m: ToResponseMarshaller[A]
  ): ToResponseMarshaller[IO[A]] =
    Marshaller(implicit ec => _.unsafeToFuture().flatMap(m(_)))

object CatsEffectsMarshallers extends CatsEffectsMarshallers
