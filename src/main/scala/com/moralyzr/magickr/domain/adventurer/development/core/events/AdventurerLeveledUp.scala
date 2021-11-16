package com.moralyzr.magickr.domain.adventurer.development.core.events

case class AdventurerLeveledUp(val adventurerId: Long, val oldLevel: Long, val newLevel: Long)
