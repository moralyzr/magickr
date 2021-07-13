package com.moralyzr.magickr.infrastructure.config

import cats.effect.IO
import cats.effect.kernel.Resource
import com.moralyzr.magickr.infrastructure.database.DatabaseConfig
import com.moralyzr.magickr.infrastructure.httpserver.AkkaHttpConfig
import com.moralyzr.magickr.security.core.types.PasswordConfig
import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.sslconfig.util.ConfigLoader

object MagickrConfigs:
  def makeConfigs(): Resource[IO, Config] =
    Resource.make(IO(ConfigFactory.load()))(_ => IO.unit)
