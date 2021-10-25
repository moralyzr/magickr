package com.moralyzr.magickr.domain.adventurer.core.ports.incoming

import cats.data.EitherT
import com.moralyzr.magickr.domain.adventurer.core.commands.RecruitNewAdventurerCommand
import com.moralyzr.magickr.domain.adventurer.core.models.Adventurer
import com.moralyzr.magickr.infrastructure.errorhandling.Problem

trait RecruitAdventurer[F[_]]:
  def recruit(userId: Long, command: RecruitNewAdventurerCommand): EitherT[F, Problem, Adventurer]
