package com.moralyzr.magickr

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.server.Directives.{complete, pathPrefix}
import akka.stream.Materializer
import cats.effect.{ExitCode, IO, IOApp}
import cats.effect.kernel.Resource
import com.moralyzr.magickr.infrastructure.actors.ActorsSystemResource
import com.moralyzr.magickr.infrastructure.config.MagickrConfigs
import com.moralyzr.magickr.infrastructure.httpserver.{AkkaHttpConfig, AkkaHttpResource}

import javax.swing.plaf.ColorUIResource
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object Magickr extends IOApp :

  override def run(args: List[String]): IO[ExitCode] =
    val server = for {
      configs <- MagickrConfigs.makeConfigs()
      httpConfigs <- AkkaHttpConfig(configs)
      actorsSystem <- ActorsSystemResource.makeActorSystem()
      streamMaterializer <- ActorsSystemResource.makeActorsMaterializer(actorsSystem)
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
