package com.moralyzr.magickr.infrastructure.database.flyway

import cats.effect.kernel.Sync
import cats.effect.IO
import cats.effect.kernel.Resource
import org.flywaydb.core.api.configuration.FluentConfiguration
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory
import com.moralyzr.magickr.infrastructure.database.DatabaseConfig
import org.flywaydb.core.api.Location
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.output.ValidateResult
import org.flywaydb.core.api.output.MigrateResult

import scala.jdk.CollectionConverters._

object DbMigrations:
  private lazy val logger: Logger = Logger(LoggerFactory.getLogger(this.getClass.getName))

  def migrateResource(flywayConfig: FlywayConfig, databaseConfig: DatabaseConfig): Resource[IO, MigrationResult] =
    Resource.make(migrate(flywayConfig, databaseConfig))(_ => IO.unit)

  def migrate(flywayConfig: FlywayConfig, databaseConfig: DatabaseConfig): IO[MigrationResult] =
    Sync[IO].delay {
      val flyway = buildFlyway(flywayConfig, databaseConfig)
      logger.info(s"Running Flyway migrations at ${flywayConfig.migrationsLocations.mkString(",")}")
      runMigration(flyway)
    }

  private def buildFlyway(flywayConfig: FlywayConfig, databaseConfig: DatabaseConfig): Flyway =
    Flyway.configure()
      .dataSource(
        databaseConfig.url,
        databaseConfig.user,
        databaseConfig.password,
      )
      .group(true)
      .outOfOrder(false)
      .table(flywayConfig.migrationsTable)
      .locations(
        flywayConfig.migrationsLocations
          .map(new Location(_))
          .toList: _*
      )
      .baselineOnMigrate(true)
      .ignorePendingMigrations(true)
      .load()

  private def runMigration(flyway: Flyway): MigrationResult =
    val validated = flyway.validateWithResult()
    validated.validationSuccessful match {
      case true  => MigrationResult.Success(flyway.migrate().migrationsExecuted)
      case false => failedMigrationResult(validated)
    }

  private def successMigrationOutput(migrateResult: MigrateResult): MigrationResult =
    migrateResult.migrations.forEach { migration =>
      logger.info(s"""
                     | Successfully Migrated
                     |  - type:  ${migration.`type`}
                     |  - category: ${migration.category}
                     |  - description: ${migration.description}
                     |  - filepath: ${migration.filepath}
                     |  - version: ${migration.version}
                     |  - Execution time: ${migration.executionTime}
                   """.stripMargin
      )
    }
    MigrationResult.Success(migrateResult.migrationsExecuted)

  private def failedMigrationResult(validateResult: ValidateResult): MigrationResult =
    for (error <- validateResult.invalidMigrations.asScala)
      logger.error(s"""
                      |Failed validation:
                      |  - version: ${error.version}
                      |  - path: ${error.filepath}
                      |  - description: ${error.description}
                      |  - errorCode: ${error.errorDetails.errorCode}
                      |  - errorMessage: ${error.errorDetails.errorMessage}
                    """.stripMargin.strip
      )
    MigrationResult.Failed(validateResult.getAllErrorMessages)
