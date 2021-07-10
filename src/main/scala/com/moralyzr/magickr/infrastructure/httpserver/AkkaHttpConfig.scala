package com.moralyzr.magickr.infrastructure.httpserver

trait AkkaHttpConfig {
  def host(): String

  def port(): Int
}
