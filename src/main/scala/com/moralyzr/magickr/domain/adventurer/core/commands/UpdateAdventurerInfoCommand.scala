package com.moralyzr.magickr.domain.adventurer.core.commands

case class UpdateAdventurerInfoCommand(
  val name  : String,
  val avatar: Option[String] = None,
  val title : Option[String] = None,
)
