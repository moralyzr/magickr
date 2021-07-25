package com.moralyzr.magickr.infrastructure.database.flyway

import cats.effect.IO
import cats.effect.kernel.{Resource, Sync}
import com.typesafe.config.Config

import scala.jdk.CollectionConverters.*

case class FlywayConfig(
    migrationsTable: String,
    migrationsLocations: List[String]
)

object FlywayConfig:
  def apply[F[_]: Sync](config: Config) = new FlywayConfig(
    migrationsTable =
      config.getString("server.database.flyway.migrations-table"),
    migrationsLocations = config
      .getStringList("server.database.flyway.migration-paths")
      .asScala
      .toList
  )
