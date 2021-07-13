package com.moralyzr.magickr.infrastructure.httpserver

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import cats.effect.IO
import cats.effect.kernel.Resource
import com.moralyzr.magickr.infrastructure.actors.ActorsSystemResource
import com.moralyzr.magickr.infrastructure.httpserver.AkkaHttpConfig
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

import scala.concurrent.Future

object AkkaHttpResource:
  private lazy val logger: Logger = Logger(LoggerFactory.getLogger(this.getClass.getName))

  def makeHttpServer(
    implicit akkaHttpConfig: AkkaHttpConfig,
    routes: Route,
    actorSystem: ActorSystem,
    materializer: Materializer,
  ): Resource[IO, Future[Http.ServerBinding]] =
    Resource.make(startHttpServer())(stopHttp())

  private def startHttpServer()(
    implicit akkaHttpConfig: AkkaHttpConfig,
    routes: Route,
    actorSystem: ActorSystem,
    materializer: Materializer,
  ) = IO {
    logger.info(s"Starting HTTP server at ${akkaHttpConfig.host()}:${akkaHttpConfig.port()}.")
    Http()
      .newServerAt(akkaHttpConfig.host(), akkaHttpConfig.port())
      .withMaterializer(materializer)
      .bind(routes)
  }

  private def stopHttp()(implicit actorSystem: ActorSystem) = (server: Future[Http.ServerBinding]) => IO {
    logger.info("Stopping HTTP Server.")
    implicit val executionContext = actorSystem.dispatcher
    server.flatMap(_.unbind())
    logger.info("Http Server stopped.")
  }
