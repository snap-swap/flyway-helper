package com.snapswap.flyway

import com.opentable.db.postgres.embedded.EmbeddedPostgres
import org.scalatest.{AsyncWordSpec, Matchers}
import slick.jdbc.H2Profile.api._

import scala.concurrent.ExecutionContext.Implicits.global

class SchemaMigrationSpec
  extends AsyncWordSpec
    with Matchers {

  import SchemaMigrationHelper._

  "FlywayPreparer" should {
    "correct apply migration" in {
      val epg = EmbeddedPostgres.start
      val db = Database.forDataSource(epg.getPostgresDatabase, None)
        .withMigration(Seq("db/migration"))
      val result = db.run(sql"""select * from test_data;""".as[(String, String)])

      result map { rs =>
        rs should have size 1
      }
    }
  }
}