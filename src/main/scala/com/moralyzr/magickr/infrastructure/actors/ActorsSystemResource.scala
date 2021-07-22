package com.moralyzr.magickr.infrastructure.actors

import akka.actor.{ActorSystem, Terminated}
import akka.stream.Materializer
import scala.concurrent.ExecutionContext
import cats.{Applicative, Functor, Monad}
import cats.effect.kernel.{Resource, Sync}
import com.typesafe.scalalogging.{LazyLogging, Logger}
import org.slf4j.LoggerFactory

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class ActorsSystemResource[F[_] : Sync]:
  private lazy val logger: Logger =
    Logger(LoggerFactory.getLogger(this.getClass.getName))

  private def makeActorSystem(): Resource[F, ActorSystem] =
    Resource.make(startActorSystem)(as => stopActorSystem(as))

  private def startActorSystem: F[ActorSystem] = Sync[F].delay {
    logger.info("Starting Akka Actors System.")
    ActorSystem("MagickrActorsSystem")
  }

  private def stopActorSystem(as: ActorSystem): F[Unit] = Sync[F].delay {
    logger.info("Shutting down Akka actors system.")
    val terminated = Await.result(as.terminate(), Duration.Inf)
    logger.info("Akka actors system terminated.")
  }

object ActorsSystemResource:
  def apply[F[_] : Sync]() =
    val actors = new ActorsSystemResource[F]
    actors.makeActorSystem()
