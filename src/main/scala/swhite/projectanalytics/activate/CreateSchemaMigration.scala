package swhite.projectanalytics.activate

import java.util.Date

import net.fwbrasil.activate.migration.Migration
import ProjectContext._

class CreateSchemaMigration extends Migration {

	def timestamp = 201225081211l

	def up {
    table[GitCommitRecord]
      .createTable(
        _.column[String]("commitID"),
        _.column[String]("filename"),
        _.column[String]("authorEmail"),
        _.column[Int]("linesAdded"),
        _.column[Int]("linesDeleted"),
        _.column[Date]("commitDate"))
    table[GitCommitRecord]
      .addIndex("idx_commitDate")("commitDate")
      .ifNotExists
    table[GitCommitRecord]
      .addIndex("idx_authorEmail")("authorEmail")
      .ifNotExists

    table[LineCountByMonthAndAuthor]
      .createTable(
          _.column[String]("authorEmail"),
          _.column[String]("month"),
          _.column[Int]("linesAdded"),
          _.column[Int]("linesDeleted"))

    }
}