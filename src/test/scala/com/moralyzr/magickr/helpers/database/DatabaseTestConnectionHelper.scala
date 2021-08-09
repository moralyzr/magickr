package com.moralyzr.magickr.helpers.database

import com.moralyzr.magickr.infrastructure.database.doobie.DatabaseConnection
import com.moralyzr.magickr.infrastructure.database.DatabaseConfig
import cats.effect.IO
import cats.effect.unsafe.implicits.global
import com.moralyzr.magickr.infrastructure.database.flyway.FlywayConfig
import cats.effect.kernel.Resource
import com.moralyzr.magickr.infrastructure.database.flyway.{
  DbMigrations,
  FlywayConfig
}

object DatabaseTestConnectionHelper {

  private val dbConfig = new DatabaseConfig(
    className = "org.h2.Driver",
    url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
    user = "sa",
    password = "",
    poolSize = 1
  )

  private val flywayConfig = new FlywayConfig(
    migrationsTable = "migrations",
    migrationsLocations = List("classpath:/db/migrations")
  )

  def buildTransactor() =
    DbMigrations.migrate[IO](flywayConfig, dbConfig).unsafeRunSync()
    DatabaseConnection.makeTransactor[IO](dbConfig)

}
