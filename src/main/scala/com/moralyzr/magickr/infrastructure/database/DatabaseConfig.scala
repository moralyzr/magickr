package com.moralyzr.magickr.infrastructure.database

import cats.effect.IO
import cats.effect.kernel.Resource
import com.moralyzr.magickr.infrastructure.httpserver.AkkaHttpConfig
import com.typesafe.config.Config

class DatabaseConfig(val config: Config) {
  def className(): String =
    config.getString("server.database.class-name")

  def url(): String =
    config.getString("server.database.url")

  def user(): String =
    config.getString("server.database.user")

  def password(): String =
    config.getString("server.database.password")

  def poolSize(): Int =
    config.getInt("server.database.pool-size")
}

object DatabaseConfig:
  def apply(config: Config): Resource[IO, DatabaseConfig] =
    Resource.make(IO(new DatabaseConfig(config)))(_ => IO.unit)
