package com.moralyzr.magickr.infrastructure.database

import cats.effect.IO
import cats.effect.kernel.{Resource, Sync}
import com.moralyzr.magickr.infrastructure.httpserver.AkkaHttpConfig
import com.typesafe.config.Config

final case class DatabaseConfig(
  className: String,
  url: String,
  user: String,
  password: String,
  poolSize: Int,
)

object DatabaseConfig:
  def apply[F[_] : Sync](config: Config) = Sync[F].delay {
    new DatabaseConfig(
      className = config.getString("server.database.class-name"),
      url = config.getString("server.database.url"),
      user = config.getString("server.database.user"),
      password = config.getString("server.database.password"),
      poolSize = config.getInt("server.database.pool-size")
    )
  }

