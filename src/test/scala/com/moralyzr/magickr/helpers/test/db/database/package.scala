package com.moralyzr.magickr.helpers
package test.db

import cats.effect.kernel.Resource
import cats.effect.{Async, IO}
import cats.syntax.all.*
import doobie.util.transactor.Transactor
import cats.effect.unsafe.implicits.global
import com.moralyzr.magickr.infrastructure.config.MagickrConfigs
import com.moralyzr.magickr.infrastructure.database.DatabaseConfig
import com.moralyzr.magickr.infrastructure.database.flyway.{DbMigrations, FlywayConfig}
import com.moralyzr.magickr.infrastructure.http.HttpConfig

import scala.concurrent.ExecutionContext

package object database {

  def getTransactor[F[_]: Async](config: DatabaseConfig): Transactor[F] =
    Transactor.fromDriverManager[F](config.className, config.url, config.user, config.password)

  def initializedTransactor[F[_]: Async]: F[Transactor[F]] = for {
    configs <- MagickrConfigs.makeConfigs[F]()
    databaseConfigs = DatabaseConfig[F](configs)
    flywayConfigs = FlywayConfig[F](configs)
    _ <- DbMigrations.migrate[F](flywayConfigs, databaseConfigs)
  } yield getTransactor(databaseConfigs)

  lazy val testEc: ExecutionContext = ExecutionContext.Implicits.global
  lazy val testTransactor: Transactor[IO] = initializedTransactor[IO].unsafeRunSync()
}
