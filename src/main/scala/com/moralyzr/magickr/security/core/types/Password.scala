package com.moralyzr.magickr.security.core.types

import com.github.t3hnar.bcrypt.*
import scala.util.matching.Regex

trait PasswordConfig:
  def rounds: Int

object PasswordType:
  opaque type Password = String
  private val bcryptPattern: Regex = "\\A\\$2a?\\$\\d\\d\\$[./0-9A-Za-z]{53}".r

  def fromString(password: String)(
    implicit config: PasswordConfig = new PasswordConfig {
    override def rounds: Int = 5
  }
  ): Password = bcryptPattern.findFirstMatchIn(password) match {
    case Some(_) => password
    case None    => password.bcryptBounded(config.rounds)
  }
