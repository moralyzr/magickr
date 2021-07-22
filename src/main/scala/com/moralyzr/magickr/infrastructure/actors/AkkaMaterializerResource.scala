package com.moralyzr.magickr.infrastructure.actors

import akka.actor.ActorSystem
import akka.stream.Materializer
import cats.effect.kernel.{Resource, Sync}
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory

class AkkaMaterializerResource[F[_] : Sync](val actorSystem: ActorSystem):
  private lazy val logger: Logger =
    Logger(LoggerFactory.getLogger(this.getClass.getName))

  def makeActorsMaterializer: Resource[F, Materializer] =
    Resource.make(startMaterializer)(stopMaterializer)

  private def startMaterializer: F[Materializer] = Sync[F].delay {
    logger.info("Starting Akka Streams Materializer.")
    Materializer(actorSystem)
  }

  private def stopMaterializer = (mt: Materializer) => Sync[F].delay {
    logger.info("Stopping Akka Streams Materializer.")
    mt.shutdown()
  }

object AkkaMaterializerResource:
  def apply[F[_] : Sync](actorSystem: ActorSystem) =
    val materializer = new AkkaMaterializerResource[F](actorSystem = actorSystem)
    materializer.makeActorsMaterializer
