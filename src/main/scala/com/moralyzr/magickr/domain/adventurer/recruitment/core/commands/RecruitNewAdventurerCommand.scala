package com.moralyzr.magickr.domain.adventurer.recruitment.core.commands

case class RecruitNewAdventurerCommand(
  val name  : String,
  val avatar: Option[String] = None,
  val title : Option[String] = None,
)
