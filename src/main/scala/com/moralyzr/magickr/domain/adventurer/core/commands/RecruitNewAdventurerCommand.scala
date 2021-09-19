package com.moralyzr.magickr.domain.adventurer.core.commands

case class RecruitNewAdventurerCommand(
  val userId: Long,
  val name  : String,
  val avatar: String,
  val title : Option[String] = None,
)
