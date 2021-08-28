package com.moralyzr.magickr.security.core.ports.outgoing

import cats.data.{EitherT, OptionT}
import cats.effect.kernel.Async
import com.moralyzr.magickr.infrastructure.errorhandling.Problem
import com.moralyzr.magickr.security.core.errors.AuthError
import com.moralyzr.magickr.security.core.models.User

trait PersistUser[F[_] : Async]:
  def save(user: User): F[User]

  def update(user: User): OptionT[F, User]
