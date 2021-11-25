package com.moralyzr.magickr.domain.adventurer.development.core.errors

import com.moralyzr.magickr.infrastructure.errorhandling.Problem

sealed trait ExperienceManagerError extends Problem

case class AdventurerLevelNotFound() extends ExperienceManagerError:
  override val url: String = ""
  override val code: String = ""
  override val title: String = ""
  override val message: String = ""

case class LevelInformationNotFound() extends ExperienceManagerError:
  override val url: String = ""
  override val code: String = ""
  override val title: String = ""
  override val message: String = ""
