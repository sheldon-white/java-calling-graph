package swhite.projectanalytics.activate

import java.util.Date

import net.fwbrasil.activate.migration.Migration
import projectContext._

class CreateSchemaMigration extends Migration {

	def timestamp = 201225081211l

	def up {
    table[GitCommitRecord]
      .createTable(
        _.column[String]("filename"),
        _.column[String]("authorEmail"),
        _.column[Int]("linesAdded"),
        _.column[Date]("commitDate"))
  }
}