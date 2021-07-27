package com.moralyzr.magickr.security.core.ports.outgoing

import cats.effect.kernel.Async
import com.moralyzr.magickr.security.core.models.User
import cats.data.EitherT
import com.moralyzr.magickr.infrastructure.errorhandling.Problem
import cats.data.OptionT

trait PersistUser[F[_] : Async]: 
    def save(user: User): F[User]
    def update(user: User): OptionT[F, User]
