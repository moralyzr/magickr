package com.moralyzr.magickr.infrastructure.actors

import akka.actor.{ActorSystem, Terminated}
import akka.stream.Materializer
import cats.effect.IO
import cats.effect.kernel.Resource
import com.moralyzr.magickr.infrastructure.actors.ActorsSystemResource.logger
import com.typesafe.scalalogging.{LazyLogging, Logger}
import org.slf4j.LoggerFactory

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object ActorsSystemResource:
  private lazy val logger: Logger =
    Logger(LoggerFactory.getLogger(this.getClass.getName))

  def makeActorSystem(): Resource[IO, ActorSystem] =
    Resource.make(startActorSystem)(stopActorSystem)

  private def startActorSystem: IO[ActorSystem] = IO {
    logger.info("Starting Akka Actors System.")
    ActorSystem("MagickrActorsSystem")
  }

  private def stopActorSystem = (as: ActorSystem) => IO {
    logger.info("Shutting down Akka actors system.")
    val terminated = Await.result(as.terminate(), Duration.Inf)
    logger.info("Akka actors system terminated.")
  }

  def makeActorsMaterializer(implicit actorSystem: ActorSystem): Resource[IO, Materializer] =
    Resource.make(startMaterializer)(stopMaterializer)

  private def startMaterializer(implicit actorSystem: ActorSystem): IO[Materializer] = IO {
    logger.info("Starting Akka Streams Materializer.")
    Materializer(actorSystem)
  }

  private def stopMaterializer = (mt: Materializer) => IO {
    logger.info("Stopping Akka Streams Materializer.")
    mt.shutdown()
  }
