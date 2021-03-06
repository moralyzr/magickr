package com.moralyzr.magickr.domain.adventurer.core.mappers

import com.moralyzr.magickr.domain.adventurer.core.commands.{RecruitNewAdventurerCommand, UpdateAdventurerInfoCommand}
import com.moralyzr.magickr.domain.adventurer.core.models.Adventurer

object AdventurerMapper:
  def fromRecruitCommand(userId: Long, command: RecruitNewAdventurerCommand): Adventurer = new Adventurer(
    userId = userId,
    name = command.name,
    title = command.title,
    avatar = command.avatar.getOrElse("/img/avatar/default.png"),
    level = 1,
    currentExperience = 1L,
  )

  def fromUpdateInfoCommand(adventurer: Adventurer, command: UpdateAdventurerInfoCommand): Adventurer = adventurer.copy(
    name = command.name,
    title = command.title,
    avatar = command.avatar.getOrElse("/img/avatar/default.png"),
  )
