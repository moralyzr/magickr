package com.moralyzr.magickr.infrastructure.errorhandling

import org.http4s.dsl.Http4sDsl
import org.http4s.Status

trait Problem:
  val url: String
  val code: String
  val title: String
  val message: String
  val statusCode: Int


