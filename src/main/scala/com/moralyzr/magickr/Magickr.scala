package com.moralyzr.magickr

import cats.effect.{ExitCode, IO, IOApp}
import cats.effect.kernel.Resource
import com.moralyzr.magickr.infrastructure.config.MagickrConfigs
import com.moralyzr.magickr.infrastructure.database.DatabaseConfig
import com.moralyzr.magickr.infrastructure.database.doobie.DatabaseConnection
import com.moralyzr.magickr.security.adapters.output.databases.postgres.UserRepository
import com.moralyzr.magickr.security.adapters.output.security.internal.{InternalAuthentication, JwtBuilder}
import com.moralyzr.magickr.security.core.SecurityManagement
import com.moralyzr.magickr.security.core.interpreters.{PasswordValidationInterpreter, UserValidationInterpreter}
import com.moralyzr.magickr.security.core.types.JwtConfig
import com.moralyzr.magickr.infrastructure.database.flyway.{DbMigrations, FlywayConfig}
import org.http4s.server.{Router, Server => H4Server}
import org.http4s.blaze.server.BlazeServerBuilder

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import com.moralyzr.magickr.security.adapters.input.rest.SecurityApi
import doobie.util.ExecutionContexts
import com.moralyzr.magickr.infrastructure.http.HttpConfig

import org.http4s.implicits.*
import cats.implicits.*
import cats.effect.implicits.*

import java.security.Security

object Magickr extends IOApp :

  override def run(args: List[String]): IO[ExitCode] =
    val server = for {
      // ThreadPools
      serverEc <- ExecutionContexts.cachedThreadPool[IO]
      // Configs
      configs <- Resource.eval(MagickrConfigs.makeConfigs[IO]())
      httpConfigs = HttpConfig[IO](configs)
      databaseConfigs = DatabaseConfig[IO](configs)
      flywayConfigs = FlywayConfig[IO](configs)
      jwtConfig = JwtConfig[IO](configs)
      // Database
      _ <- Resource.eval(
        DbMigrations.migrate[IO](flywayConfigs, databaseConfigs)
      )
      transactor <- DatabaseConnection.makeTransactor[IO](databaseConfigs)
      userRepository = UserRepository[IO](transactor)
      // Interpreters
      jwtManager = JwtBuilder[IO](jwtConfig)
      authentication = InternalAuthentication[IO](
        passwordValidationAlgebra = new PasswordValidationInterpreter(),
        jwtManager = jwtManager
      )
      userValidation = UserValidationInterpreter[IO](userRepository)
      // Services
      securityManagement = SecurityManagement[IO](
        findUser = userRepository,
        persistUser = userRepository,
        authentication = authentication,
        userValidationAlgebra = userValidation
      )
      // Api
      httpRoutes = Router(
        "/api/user" -> SecurityApi.endpoints[IO](securityManagement)
      ).orNotFound
      // Blaze Server
      server <- BlazeServerBuilder[IO](serverEc)
        .bindHttp(httpConfigs.port, httpConfigs.host)
        .withHttpApp(httpRoutes)
        .resource
    } yield (server)
    return server.use(_ => IO.never).as(ExitCode.Success)
