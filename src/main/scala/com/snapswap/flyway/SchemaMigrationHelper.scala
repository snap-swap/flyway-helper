package com.snapswap.flyway

import slick.jdbc.PostgresProfile.api._

trait SchemaMigrationHelper {
  implicit def withMigration(db: Database): SchemaMigration = {
    new SchemaMigration(db)
  }
}

object SchemaMigrationHelper extends SchemaMigrationHelper