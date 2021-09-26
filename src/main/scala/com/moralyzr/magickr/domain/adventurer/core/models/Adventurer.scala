package com.moralyzr.magickr.domain.adventurer.core.models

case class Adventurer(
  val id               : Option[Long] = None,
  val userId           : Long,
  val avatar           : String,
  val name             : String,
  val title            : Option[String] = None,
  val level            : Int,
  val currentExperience: Long,
)
