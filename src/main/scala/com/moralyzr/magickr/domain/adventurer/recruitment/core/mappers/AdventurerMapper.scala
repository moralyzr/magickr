package com.moralyzr.magickr.domain.adventurer.recruitment.core.mappers

import com.moralyzr.magickr.domain.adventurer.recruitment.core.commands.{
  RecruitNewAdventurerCommand,
  UpdateAdventurerInfoCommand,
}
import com.moralyzr.magickr.domain.adventurer.recruitment.core.models.Adventurer

object AdventurerMapper:

  def fromRecruitCommand(userId: Long, command: RecruitNewAdventurerCommand): Adventurer = Adventurer(
    userId = userId,
    name = command.name,
    title = command.title,
    avatar = command.avatar.getOrElse("/img/avatar/default.png"),
  )

  def fromUpdateInfoCommand(adventurer: Adventurer, command: UpdateAdventurerInfoCommand): Adventurer =
    adventurer.copy(
      name = command.name,
      title = command.title,
      avatar = command.avatar.getOrElse("/img/avatar/default.png"),
    )
