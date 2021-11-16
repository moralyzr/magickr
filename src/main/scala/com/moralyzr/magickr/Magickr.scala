package com.moralyzr.magickr

import cats.effect.{ExitCode, IO, IOApp}
import cats.effect.kernel.Resource
import com.moralyzr.magickr.infrastructure.config.MagickrConfigs
import com.moralyzr.magickr.infrastructure.database.DatabaseConfig
import com.moralyzr.magickr.infrastructure.database.doobie.DatabaseConnection
import com.moralyzr.magickr.domain.security.adapters.output.security.internal.JwtBuilder
import com.moralyzr.magickr.infrastructure.database.flyway.{DbMigrations, FlywayConfig}
import org.http4s.server.{Router, Server as H4Server}
import org.http4s.blaze.server.BlazeServerBuilder

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import doobie.util.ExecutionContexts
import com.moralyzr.magickr.infrastructure.http.HttpConfig
import org.http4s.implicits.*
import cats.implicits.*
import cats.effect.implicits.*
import com.moralyzr.magickr.domain.adventurer.recruitment.adapters.output.databases.sql.AdventurerRepository
import com.moralyzr.magickr.domain.adventurer.recruitment.core.validations.interpreters.AdventurerValidationInterpreter
import com.moralyzr.magickr.domain.adventurer.recruitment.core.AdventurerBarracks
import com.moralyzr.magickr.domain.security.adapters.input.rest.SecurityApi
import com.moralyzr.magickr.domain.security.adapters.output.databases.postgres.UserRepository
import com.moralyzr.magickr.domain.security.adapters.output.security.internal.{InternalAuthentication, JwtBuilder}
import com.moralyzr.magickr.domain.security.core.types.JwtConfig
import com.moralyzr.magickr.domain.security.core.SecurityManagement
import com.moralyzr.magickr.domain.security.core.interpreters.{PasswordValidationInterpreter, UserValidationInterpreter}

import java.security.Security

object Magickr extends IOApp :

  override def run(args: List[String]): IO[ExitCode] =
    val server = for {
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
      adventurerRepository = AdventurerRepository[IO](transactor)
      // Validators
      userValidation = UserValidationInterpreter[IO](userRepository)
      adventurerValidation = AdventurerValidationInterpreter[IO](adventurerRepository)
      // Services
      jwtManager = JwtBuilder[IO](jwtConfig)
      authentication = InternalAuthentication[IO](
        passwordValidationAlgebra = new PasswordValidationInterpreter(),
        jwtManager = jwtManager,
      )
      securityManagement = SecurityManagement[IO](
        findUser = userRepository,
        persistUser = userRepository,
        authentication = authentication,
        userValidationAlgebra = userValidation,
      )
      adventurerBarracks = AdventurerBarracks[IO](
        findAdventurer = adventurerRepository,
        persistAdventurer = adventurerRepository,
        adventurerValidationAlgebra = adventurerValidation,
        userValidationAlgebra = userValidation,
      )
      // Api
      httpRoutes = Router(
        "/api/user" -> SecurityApi.endpoints[IO](securityManagement)
      ).orNotFound
      // Blaze Server
      server <- BlazeServerBuilder[IO]
        .bindHttp(httpConfigs.port, httpConfigs.host)
        .withHttpApp(httpRoutes)
        .resource
    } yield (server)
    return server.use(_ => IO.never).as(ExitCode.Success)
