package com.moralyzr.magickr.infrastructure.httpserver

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import cats.effect.IO
import cats.effect.kernel.Resource
import com.moralyzr.magickr.infrastructure.actors.ActorsSystemResource
import com.moralyzr.magickr.infrastructure.httpserver.AkkaHttpConfig
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

class AkkaHttpResource private(
  val akkaHttpConfig: AkkaHttpConfig,
  val routes: Route,
  val actorsResource: ActorsSystemResource,
):
  private lazy val logger: Logger = Logger(LoggerFactory.getLogger(this.getClass.getName))

  private implicit val actorSystem: ActorSystem = actorsResource.actorSystem

  lazy val httpServer = Http()
    .newServerAt(akkaHttpConfig.host(), akkaHttpConfig.port())
    .withMaterializer(actorsResource.materializer)
    .bind(routes)

  def close(): IO[Unit] = IO {
    logger.info("Stopping HTTP Server.")
    implicit val executionContext = actorSystem.dispatcher
    httpServer.flatMap(_.unbind())
    logger.info("Http Server stopped.")
  }


object AkkaHttpResource {
  private lazy val logger: Logger = Logger(LoggerFactory.getLogger(this.getClass.getName))

  def load(
    akkaHttpConfig: AkkaHttpConfig,
    routes: Route,
    actorsResource: ActorsSystemResource,
  ) =
    logger.info(s"Starting HTTP server at ${akkaHttpConfig.host()}:${akkaHttpConfig.port()}/")
    Resource.make(IO.pure(new AkkaHttpResource(akkaHttpConfig, routes, actorsResource)))(_.close())
}
