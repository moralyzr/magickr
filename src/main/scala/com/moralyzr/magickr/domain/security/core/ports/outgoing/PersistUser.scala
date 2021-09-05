package com.moralyzr.magickr.domain.security.core.ports.outgoing

import cats.data.{EitherT, OptionT}
import cats.effect.kernel.Async
import com.moralyzr.magickr.domain.security.core.errors.AuthError
import com.moralyzr.magickr.domain.security.core.models.User
import com.moralyzr.magickr.infrastructure.errorhandling.Problem

trait PersistUser[F[_] : Async]:
  def save(user: User): F[User]

  def update(user: User): OptionT[F, User]
