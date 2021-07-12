package com.moralyzr.magickr.infrastructure.database.doobie

import cats.effect.IO
import com.moralyzr.magickr.infrastructure.database.DatabaseConfig
import doobie.util.ExecutionContexts
import doobie.util.transactor.Transactor

class DatabaseConnection(val config: DatabaseConfig):
  val xa = Transactor.fromDriverManager[IO](
    config.className(),
    config.url(),
    config.user(),
    config.password(),
  )

