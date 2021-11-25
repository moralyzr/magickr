package com.moralyzr.magickr.domain.adventurer.recruitment.core.errors

import com.moralyzr.magickr.infrastructure.errorhandling.Problem

sealed trait AdventurerError extends Problem

case class AdventurerNotFound() extends AdventurerError:
  override val url: String = "/problem/adventurer/not-found"
  override val code: String = "ADVENTURER_NOT_FOUND"
  override val title: String = "Adventurer not found"
  override val message: String = "The informed adventurer does not exists!"

case class AdventurerAlreadyExists() extends AdventurerError:
  override val url: String = "/problem/adventurer/already-exists"
  override val code: String = "ADVENTURER_ALREADY_EXISTS"
  override val title: String = "Adventurer Already Exists"
  override val message: String = "The informed adventurer already exists!"

case class FailedToUpdateAdventurer() extends AdventurerError:
  override val url: String = "/problem/adventurer/failed-update"
  override val code: String = "FAILED_TO_UPDATE_ADVENTURER"
  override val title: String = "Update failed"
  override val message: String = "The informed adventurer already exists!"
