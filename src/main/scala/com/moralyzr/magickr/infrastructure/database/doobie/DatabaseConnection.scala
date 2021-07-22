package com.moralyzr.magickr.infrastructure.database.doobie

import cats.effect.IO
import cats.effect.kernel.{Resource, Async}
import cats.{Applicative, Monad}
import com.moralyzr.magickr.infrastructure.database.DatabaseConfig
import com.typesafe.scalalogging.Logger
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts
import doobie.util.transactor.Transactor
import org.slf4j.LoggerFactory

object DatabaseConnection:
  private lazy val logger: Logger = Logger(LoggerFactory.getLogger(this.getClass.getName))

  def makeTransactor[F[_] : Async](config: DatabaseConfig): Resource[F, HikariTransactor[F]] =
    logger.info(s"Connecting to the Database at ${config.url}")
    for {
      ce <- ExecutionContexts.fixedThreadPool[F](size = config.poolSize)
      xa <- HikariTransactor.newHikariTransactor[F](
        driverClassName = config.className,
        url = config.url,
        user = config.user,
        pass = config.password,
        connectEC = ce,
      )
    } yield xa
