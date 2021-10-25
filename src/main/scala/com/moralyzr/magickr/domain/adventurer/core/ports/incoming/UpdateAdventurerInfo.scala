package com.moralyzr.magickr.domain.adventurer.core.ports.incoming

import cats.data.EitherT
import com.moralyzr.magickr.domain.adventurer.core.commands.UpdateAdventurerInfoCommand
import com.moralyzr.magickr.domain.adventurer.core.models.Adventurer
import com.moralyzr.magickr.infrastructure.errorhandling.Problem

trait UpdateAdventurerInfo[F[_]]:
  def update(userId: Long, command: UpdateAdventurerInfoCommand): EitherT[F, Problem, Adventurer]
