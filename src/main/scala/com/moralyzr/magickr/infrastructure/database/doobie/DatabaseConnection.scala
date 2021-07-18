package com.moralyzr.magickr.infrastructure.database.doobie

import cats.effect.IO
import cats.effect.kernel.Resource
import com.moralyzr.magickr.infrastructure.database.DatabaseConfig
import com.typesafe.scalalogging.Logger
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts
import doobie.util.transactor.Transactor
import org.slf4j.LoggerFactory

object DatabaseConnection:
  private lazy val logger: Logger = Logger(LoggerFactory.getLogger(this.getClass.getName))

  def makeTransactor(config: DatabaseConfig): Resource[IO, HikariTransactor[IO]] =
    logger.info(s"Connecting to the Database at ${config.url}")
    for {
      ce <- ExecutionContexts.fixedThreadPool[IO](size = config.poolSize)
      xa <- HikariTransactor.newHikariTransactor[IO](
        driverClassName = config.className,
        url = config.url,
        user = config.user,
        pass = config.password,
        connectEC = ce,
      )
    } yield xa
