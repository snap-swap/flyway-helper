package com.snapswap.flyway

import org.flywaydb.core.Flyway
import slick.jdbc.DataSourceJdbcDataSource
import slick.jdbc.PostgresProfile.api._
import slick.jdbc.hikaricp.HikariCPJdbcDataSource

import scala.util.control.NonFatal

class SchemaMigration(val db: Database) {
  def withMigration(dir: Seq[String], schema: String = "public", baseline: Boolean = false, shutdown: => Unit = {}): Database = {
    val flyway = new Flyway()
    val ds = db.source match {
      case d: DataSourceJdbcDataSource =>
        d.ds
      case d: HikariCPJdbcDataSource =>
        d.ds
      case other =>
        throw new IllegalStateException("Unknown DataSource type: " + other)
    }

    flyway.setDataSource(ds)
    flyway.setLocations(dir: _*)
    flyway.setBaselineOnMigrate(baseline)
    flyway.setSchemas(schema)

    try {
      flyway.migrate()
    } catch {
      case NonFatal(ex) =>
        ex.printStackTrace()
        shutdown
    }

    db
  }
}