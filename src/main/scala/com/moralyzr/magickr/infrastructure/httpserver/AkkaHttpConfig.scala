package com.moralyzr.magickr.infrastructure.httpserver

import cats.effect.IO
import cats.effect.kernel.Resource
import com.typesafe.config.Config

class AkkaHttpConfig private(val config: Config):
  def host(): String = config.getString("server.http.host")

  def port(): Int = config.getInt("server.http.port")

object AkkaHttpConfig:
  def apply(config: Config): Resource[IO, AkkaHttpConfig] =
    Resource.make(IO(new AkkaHttpConfig(config)))(_ => IO.unit)

