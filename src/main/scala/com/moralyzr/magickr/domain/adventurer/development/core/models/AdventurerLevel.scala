package com.moralyzr.magickr.domain.adventurer.development.core.models

case class AdventurerLevel(
  val id: Long,
  val adventurerId: Long,
  val level: Int,
  val currentExperience: BigInt,
):

  def incrementExperience(experience: BigInt): AdventurerLevel =
    this.copy(currentExperience = currentExperience + experience)

  def decreaseExperience(experience: BigInt): AdventurerLevel =
    val newExp = currentExperience - experience
    this.copy(currentExperience = if (newExp < 0) 0 else newExp)
