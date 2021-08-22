package com.moralyzr.magickr.security.fixtures

import com.moralyzr.magickr.security.core.models.User
import com.moralyzr.magickr.security.core.types.{EmailType, PasswordType}
import faker.Faker

object UserFixtures {

  def user = new User(
    name = Faker.default.firstName(),
    lastName = Faker.default.lastName(),
    email = EmailType.fromString(Faker.default.emailAddress()),
    password = PasswordType.fromString(Faker.default.password()),
    active = false,
    birthDate = Faker.default.pastLocalDateTime().toLocalDate
  )

}
