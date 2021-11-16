package com.moralyzr.magickr.domain.adventurer.recruitment.core.commands

case class DeduceAdventurerExperienceCommand(val adventurerId: Long, val removedExperience: BigInt)
