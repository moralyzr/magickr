package com.moralyzr.magickr.infrastructure.config

import cats.effect.IO
import cats.effect.kernel.{Resource, Sync}
import com.moralyzr.magickr.infrastructure.database.DatabaseConfig
import com.moralyzr.magickr.infrastructure.http.HttpConfig
import com.typesafe.config.{Config, ConfigFactory}

object MagickrConfigs:
  def makeConfigs[F[_] : Sync]() = Sync[F].delay {
    ConfigFactory.load()
  }
