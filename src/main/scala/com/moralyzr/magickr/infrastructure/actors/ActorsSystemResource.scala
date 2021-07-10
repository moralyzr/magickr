package com.moralyzr.magickr.infrastructure.actors

import akka.actor.{ActorSystem, Terminated}
import akka.stream.Materializer
import cats.effect.IO
import cats.effect.kernel.Resource
import com.typesafe.scalalogging.{LazyLogging, Logger}
import org.slf4j.LoggerFactory

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class ActorsSystemResource:
  private lazy val logger: Logger =
    Logger(LoggerFactory.getLogger(this.getClass.getName))

  val actorSystem: ActorSystem = ActorSystem("MagickrActorsSystem")
  val materializer: Materializer = Materializer(actorSystem)

  def close(): IO[Unit] = IO {
    logger.info("Shutting down Akka actors system.")
    val terminated = Await.result(actorSystem.terminate(), Duration.Inf)
    logger.info("Akka actors system terminated.")
  }

object ActorsSystemResource:
  private lazy val logger: Logger =
    Logger(LoggerFactory.getLogger(this.getClass.getName))

  def load(): Resource[IO, ActorsSystemResource] =
    logger.info("Starting Akka Actors System.")
    var aktors = Resource.make(IO.pure(new ActorsSystemResource()))(_.close())
    logger.info("Actors System started.")
    return aktors
