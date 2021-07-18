package com.moralyzr.magickr.infrastructure.database.flyway

import cats.effect.IO
import cats.effect.kernel.Resource
import com.typesafe.config.Config
import scala.jdk.CollectionConverters._

case class FlywayConfig private(
  migrationsTable: String,
  migrationsLocations: List[String],
)

object FlywayConfig:
  def apply(config: Config): Resource[IO, FlywayConfig] =
    val flywayConfig = new FlywayConfig(
      migrationsTable = config.getString("server.database.flyway.migrations-table"),
      migrationsLocations = config.getStringList("server.database.flyway.migration-paths").asScala.toList,
    )
    Resource.make(IO.pure(flywayConfig))(_ => IO.unit)
