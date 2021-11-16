package com.moralyzr.magickr.domain.adventurer.recruitment.core.ports.incoming

import cats.data.EitherT
import com.moralyzr.magickr.domain.adventurer.recruitment.core.commands.RecruitNewAdventurerCommand
import com.moralyzr.magickr.domain.adventurer.recruitment.core.models.Adventurer
import com.moralyzr.magickr.infrastructure.errorhandling.Problem

trait RecruitAdventurer[F[_]]:
  def recruit(userId: Long, command: RecruitNewAdventurerCommand): EitherT[F, Problem, Adventurer]
