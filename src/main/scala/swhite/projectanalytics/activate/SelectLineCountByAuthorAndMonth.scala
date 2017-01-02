package swhite.projectanalytics.activate

import java.io._

import swhite.projectanalytics.activate.ProjectContext._
import swhite.projectanalytics.json.JsonUtil

object SelectLineCountByAuthorAndMonth extends App {

  class DataSet(val name: String, val x: List[String], val y: List[Int], val plotType: String = "scatter")

  transactional {
    val commitsByAuthor = query {
      (entity: LineCountByMonthAndAuthor) =>
        where() select entity orderBy (entity.month, entity.authorEmail)
    }.groupBy(gcr => gcr.authorEmail)

    val tmp = commitsByAuthor map {
      case (authorEmail, commits) =>
      new DataSet(authorEmail, commits.map( x => x.month), commits.map( x => x.linesAdded))
    }

    val pw = new PrintWriter(new File("web/datasets/commitsByMonthAndAuthor.json"))
    pw.write(JsonUtil.toJson(tmp.toSeq.sortBy(_.name)))
    pw.close()
  }
}