package com.moralyzr.magickr

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives.{complete, pathPrefix}
import akka.stream.Materializer
import cats.effect.{ExitCode, IO, IOApp}
import cats.effect.kernel.Resource
import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.server.Directives.{complete, pathPrefix}
import com.moralyzr.magickr.infrastructure.actors.{
  ActorsSystemResource,
  AkkaMaterializerResource
}
import com.moralyzr.magickr.infrastructure.config.MagickrConfigs
import com.moralyzr.magickr.infrastructure.database.DatabaseConfig
import com.moralyzr.magickr.infrastructure.database.doobie.DatabaseConnection
import com.moralyzr.magickr.infrastructure.httpserver.{
  AkkaHttpConfig,
  AkkaHttpResource
}
import com.moralyzr.magickr.security.adapters.output.databases.postgres.UserRepository
import com.moralyzr.magickr.security.adapters.output.security.internal.{
  InternalAuthentication,
  JwtBuilder
}
import com.moralyzr.magickr.security.core.SecurityManagement
import com.moralyzr.magickr.security.core.interpreters.SecurityValidationsInterpreter
import com.moralyzr.magickr.security.core.types.JwtConfig
import com.moralyzr.magickr.infrastructure.database.flyway.{
  DbMigrations,
  FlywayConfig
}

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import com.moralyzr.magickr.security.adapters.input.rest.SecurityApi
import java.security.Security

object Magickr extends IOApp:

  override def run(args: List[String]): IO[ExitCode] =
    val server = for {
      // Actors
      actorsSystem <- ActorsSystemResource[IO]()
      streamMaterializer <- AkkaMaterializerResource[IO](actorsSystem)
      // Configs
      configs <- Resource.eval(MagickrConfigs.makeConfigs[IO]())
      httpConfigs = AkkaHttpConfig[IO](configs)
      databaseConfigs = DatabaseConfig[IO](configs)
      flywayConfigs = FlywayConfig[IO](configs)
      jwtConfig = JwtConfig[IO](configs)
      // Interpreters
      jwtManager = JwtBuilder[IO](jwtConfig)
      authentication = InternalAuthentication[IO](
        passwordValidationAlgebra = new SecurityValidationsInterpreter(),
        jwtManager = jwtManager
      )
      // Database
      _ <- Resource.eval(
        DbMigrations.migrate[IO](flywayConfigs, databaseConfigs)
      )
      transactor <- DatabaseConnection.makeTransactor[IO](databaseConfigs)
      userRepository = UserRepository[IO](transactor)
      // Services
      securityManagement = SecurityManagement[IO](
        findUser = userRepository,
        authentication = authentication
      )
      // Api
      secApi = new SecurityApi[IO](securityManagement)
      routes = pathPrefix("api") {
        secApi.routes()
      }
      akkaHttp <- AkkaHttpResource.makeHttpServer[IO](
        akkaHttpConfig = httpConfigs,
        routes = routes,
        actorSystem = actorsSystem,
        materializer = streamMaterializer
      )
    } yield (actorsSystem)
    return server.useForever
