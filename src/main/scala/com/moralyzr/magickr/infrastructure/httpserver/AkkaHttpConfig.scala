package com.moralyzr.magickr.infrastructure.httpserver

import cats.effect.IO
import cats.effect.kernel.{Resource, Sync}
import com.typesafe.config.Config

final case class AkkaHttpConfig(
    host: String,
    port: Int
)

object AkkaHttpConfig:
  def apply[F[_]: Sync](config: Config) = new AkkaHttpConfig(
    host = config.getString("server.http.host"),
    port = config.getInt("server.http.port")
  )
