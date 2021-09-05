package com.moralyzr.magickr.domain.security.core.types.implicits

import com.moralyzr.magickr.domain.security.core.types.{EmailType, PasswordType}
import com.moralyzr.magickr.domain.security.core.types.EmailType.Email
import com.moralyzr.magickr.domain.security.core.types.PasswordType.Password
import doobie.util.meta.Meta

implicit val passwordMeta: Meta[Password] =
  Meta[String].timap(PasswordType.fromString(_))(_.toString)
implicit val emailMeta   : Meta[Email]    =
  Meta[String].timap(EmailType.fromString(_))(_.toString)
