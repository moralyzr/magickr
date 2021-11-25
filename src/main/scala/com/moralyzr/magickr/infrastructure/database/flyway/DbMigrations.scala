package com.moralyzr.magickr.infrastructure.database.flyway

import cats.effect.kernel.Sync
import cats.effect.IO
import cats.effect.kernel.Resource
import cats.syntax.functor.*
import org.flywaydb.core.api.configuration.FluentConfiguration
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory
import com.moralyzr.magickr.infrastructure.database.DatabaseConfig
import org.flywaydb.core.api.Location
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.output.ValidateResult
import org.flywaydb.core.api.output.MigrateResult

import scala.jdk.CollectionConverters.*

object DbMigrations:
  private lazy val logger: Logger = Logger(LoggerFactory.getLogger(this.getClass.getName))

  def migrate[F[_]](flywayConfig: FlywayConfig, databaseConfig: DatabaseConfig)(implicit
    S: Sync[F],
  ): F[Unit] = S.delay {
    val flyway = buildFlyway(flywayConfig, databaseConfig)
    logger.info(s"Running Flyway migrations at ${flywayConfig.migrationsLocations.mkString(",")}")
    flyway.migrate()
  }.as(())

  private def buildFlyway(flywayConfig: FlywayConfig, databaseConfig: DatabaseConfig): Flyway =
    Flyway.configure()
      .dataSource(databaseConfig.url, databaseConfig.user, databaseConfig.password)
      .table(flywayConfig.migrationsTable)
      .locations(
        flywayConfig.migrationsLocations
          .map(new Location(_)): _*,
      )
      .baselineOnMigrate(true)
      .load()
