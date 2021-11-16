package com.moralyzr.magickr.domain.adventurer.recruitment.core.errors

import com.moralyzr.magickr.infrastructure.errorhandling.Problem

sealed trait AdventurerError extends Problem

case class AdventurerNotFound(
  url    : String = "/problem/adventurer/not-found",
  code   : String = "ADVENTURER_NOT_FOUND",
  title  : String = "Adventurer not found",
  message: String = "The informed adventurer does not exists!",
) extends AdventurerError

case class AdventurerAlreadyExists(
  url    : String = "/problem/adventurer/already-exists",
  code   : String = "ADVENTURER_ALREADY_EXISTS",
  title  : String = "Adventurer Already Exists",
  message: String = "The informed adventurer already exists!",
) extends AdventurerError

case class FailedToUpdateAdventurer(
  url    : String = "/problem/adventurer/failed-update",
  code   : String = "FAILED_TO_UPDATE_ADVENTURER",
  title  : String = "Update failed",
  message: String = "The informed adventurer already exists!",
) extends AdventurerError
