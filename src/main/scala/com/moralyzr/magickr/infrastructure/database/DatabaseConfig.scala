package com.moralyzr.magickr.infrastructure.database

trait DatabaseConfig {
  def className(): String

  def url(): String

  def user(): String

  def password(): String

  def poolSize(): Int
}
