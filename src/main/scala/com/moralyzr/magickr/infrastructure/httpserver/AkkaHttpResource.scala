package com.moralyzr.magickr.infrastructure.httpserver

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import cats.effect.IO
import cats.effect.kernel.{Resource, Sync}
import com.moralyzr.magickr.infrastructure.actors.ActorsSystemResource
import com.moralyzr.magickr.infrastructure.httpserver.AkkaHttpConfig
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

import scala.concurrent.Future

object AkkaHttpResource:
  private lazy val logger: Logger = Logger(LoggerFactory.getLogger(this.getClass.getName))

  def makeHttpServer[F[_] : Sync](
    akkaHttpConfig: AkkaHttpConfig,
    routes: Route,
    actorSystem: ActorSystem,
    materializer: Materializer,
  ): Resource[F, Future[Http.ServerBinding]] =
    Resource.make(startHttpServer(akkaHttpConfig, routes, actorSystem, materializer))(stopHttp(actorSystem))

  private def startHttpServer[F[_] : Sync](
    akkaHttpConfig: AkkaHttpConfig,
    routes: Route,
    actorSystem: ActorSystem,
    materializer: Materializer,
  ) = Sync[F].delay {
    implicit val actors = actorSystem
    logger.info(s"Starting HTTP server at ${akkaHttpConfig.host}:${akkaHttpConfig.port}.")
    val http = Http()
      .newServerAt(akkaHttpConfig.host, akkaHttpConfig.port)
      .withMaterializer(materializer)
      .bind(routes)
    logger.info(s"The application is ready and is listening at http://${akkaHttpConfig.host}:${akkaHttpConfig.port}.")
    http
  }

  private def stopHttp[F[_] : Sync](actorSystem: ActorSystem) = (server: Future[Http.ServerBinding]) => Sync[F].delay {
    logger.info("Stopping HTTP Server.")
    implicit val executionContext = actorSystem.dispatcher
    server.flatMap(_.unbind())
    logger.info("Http Server stopped.")
  }
