package com.moralyzr.magickr.security.core.types

import org.scalatest.flatspec.AnyFlatSpec

class PasswordTypeTest extends AnyFlatSpec {
  behavior of "An Password Type"

  it should "convert the string to a BCrypt password if the String is not already Hashed" in {
    val password = PasswordType.fromString("plain-password")

    assert(password.toString != "plain-password")
  }

  it should "not Convert the string to a BCrypt password if the String is already hashed" in {
    val alreadyHashed =
      "$2a$05$UAirNV.N3I4PHgLYD1fX5epQnkpodzLs/ZIdFeYmZ4KzdDfPbLMnG"

    val password = PasswordType.fromString(alreadyHashed)

    assert(password.toString == alreadyHashed)
  }

}
