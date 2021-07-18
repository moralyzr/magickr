package com.moralyzr.magickr.infrastructure.database.flyway

enum MigrationResult:
  case Success(val executedMigrations: Int) extends MigrationResult
  case Failed(val reason: String) extends MigrationResult