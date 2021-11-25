package com.moralyzr.magickr.domain.adventurer.development.core.events

sealed class AdventurerLevelingEvent(val adventurerId: Long, val oldLevel: Long, val newLevel: Long)

case class AdventurerLeveledUp(
  override val adventurerId: Long,
  override val oldLevel: Long,
  override val newLevel: Long,
) extends AdventurerLevelingEvent(adventurerId, oldLevel, newLevel)

case class AdventurerLeveledDown(
  override val adventurerId: Long,
  override val oldLevel: Long,
  override val newLevel: Long,
) extends AdventurerLevelingEvent(adventurerId, oldLevel, newLevel)
