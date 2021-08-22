package com.moralyzr.magickr.infrastructure.errorhandling

trait Problem:
  val url: String
  val code: String
  val title: String
  val message: String
