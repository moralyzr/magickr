package com.moralyzr.magickr

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.server.Directives.{complete, pathPrefix}
import akka.stream.Materializer
import cats.effect.{ExitCode, IO, IOApp}
import cats.effect.kernel.Resource
import com.moralyzr.magickr.infrastructure.actors.ActorsSystemResource
import com.moralyzr.magickr.infrastructure.config.MagickrConfigs
import com.moralyzr.magickr.infrastructure.database.DatabaseConfig
import com.moralyzr.magickr.infrastructure.database.doobie.DatabaseConnection
import com.moralyzr.magickr.infrastructure.httpserver.{AkkaHttpConfig, AkkaHttpResource}
import com.moralyzr.magickr.security.adapters.userdata.database.UserRepository

import javax.swing.plaf.ColorUIResource
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object Magickr extends IOApp :

  override def run(args: List[String]): IO[ExitCode] =
    val server = for {
      // Configs
      configs <- MagickrConfigs.makeConfigs()
      httpConfigs <- AkkaHttpConfig(configs)
      databaseConfigs <- DatabaseConfig(configs)
      // Actors
      actorsSystem <- ActorsSystemResource.makeActorSystem()
      streamMaterializer <- ActorsSystemResource.makeActorsMaterializer(actorsSystem)
      // Database
      transactor <- DatabaseConnection.makeTransactor(databaseConfigs)
      userRepository <- UserRepository(transactor)
      // Api
      routes = pathPrefix("api") {
        Directives.get {
          complete {
            "Hello world"
          }
        }
      }
      akkaHttp <- AkkaHttpResource.makeHttpServer(
        akkaHttpConfig = httpConfigs,
        routes = routes,
        actorSystem = actorsSystem,
        materializer = streamMaterializer,
      )
    } yield (actorsSystem)
    return server.useForever
