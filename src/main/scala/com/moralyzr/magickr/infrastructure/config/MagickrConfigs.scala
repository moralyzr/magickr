package com.moralyzr.magickr.infrastructure.config

import cats.effect.IO
import cats.effect.kernel.{Resource, Sync}
import com.moralyzr.magickr.infrastructure.database.DatabaseConfig
import com.moralyzr.magickr.infrastructure.httpserver.AkkaHttpConfig
import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.sslconfig.util.ConfigLoader

object MagickrConfigs:
  def makeConfigs[F[_] : Sync]() = Sync[F].delay {
    ConfigFactory.load()
  }
