package com.moralyzr.magickr.infrastructure.http

import cats.effect.IO
import cats.effect.kernel.{Resource, Sync}
import com.typesafe.config.Config

final case class HttpConfig(
    host: String,
    port: Int
)

object HttpConfig:
  def apply[F[_]: Sync](config: Config) = new HttpConfig(
    host = config.getString("server.http.host"),
    port = config.getInt("server.http.port")
  )
