package com.moralyzr.magickr.infrastructure.database.doobie.implicits

import com.typesafe.scalalogging.Logger
import doobie.util.log.LogHandler
import org.slf4j.LoggerFactory

import doobie.util.log.{Success, ProcessingFailure, ExecFailure}

implicit val slf4jLogHandler: LogHandler =
  val logger: Logger = Logger(LoggerFactory.getLogger("DoobieSql"))
  LogHandler {
    case Success(s, a, e1, e2) =>
      logger.info(
        s"""Successful Statement Execution:
           |
           |  ${s.linesIterator.dropWhile(_.trim.isEmpty).mkString("\n  ")}
           |
           | arguments = [${a.mkString(", ")}]
           |   elapsed = ${e1.toMillis} ms exec + ${e2.toMillis} ms processing (${(e1 + e2).toMillis} ms total)
      """.stripMargin
      )

    case ProcessingFailure(s, a, e1, e2, t) =>
      logger.error(
        s"""Failed Resultset Processing:
           |
           |  ${s.linesIterator.dropWhile(_.trim.isEmpty).mkString("\n  ")}
           |
           | arguments = [${a.mkString(", ")}]
           |   elapsed = ${e1.toMillis} ms exec + ${e2.toMillis} ms processing (failed) (${(e1 + e2).toMillis} ms total)
           |   failure = ${t.getMessage}
      """.stripMargin
      )

    case ExecFailure(s, a, e1, t) =>
      logger.error(
        s"""Failed Statement Execution:
           |
           |  ${s.linesIterator.dropWhile(_.trim.isEmpty).mkString("\n  ")}
           |
           | arguments = [${a.mkString(", ")}]
           |   elapsed = ${e1.toMillis} ms exec (failed)
           |   failure = ${t.getMessage}
      """.stripMargin
      )
  }
