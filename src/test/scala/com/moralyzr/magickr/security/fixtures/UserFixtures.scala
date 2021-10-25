package com.moralyzr.magickr.domain.security.fixtures

import com.moralyzr.magickr.domain.security.core.models.User
import com.moralyzr.magickr.domain.security.core.types.{EmailType, PasswordType}
import faker.Faker

object UserFixtures {

  def user = new User(
    name = Faker.default.firstName(),
    lastName = Faker.default.lastName(),
    email = EmailType.fromString(Faker.default.emailAddress()),
    password = PasswordType.fromString(Faker.default.password()),
    active = false,
    birthDate = Faker.default.pastLocalDateTime().toLocalDate,
  )

}
