package swhite.projectanalytics.activate

import swhite.projectanalytics.activate.ProjectContext._
import swhite.projectanalytics.json.JsonUtil

object SelectLineCountByAuthorAndMonth extends App {

  class DataSet(val name: String, val x: List[String], val y: List[Int], val plotType: String = "scatter")

  transactional {
    val commitsByAuthor = query {
      (entity: LineCountByMonthAndAuthor) =>
        where() select entity orderBy (entity.month, entity.authorEmail)
    }.groupBy(gcr => gcr.authorEmail)

    val tmp = commitsByAuthor map {case (authorEmail, commits) =>
      new DataSet(authorEmail, commits.map( x => x.month), commits.map( x => x.linesAdded))}
    import java.io._
    val pw = new PrintWriter(new File("commitsByMonthAndAuthor.json"))
    pw.write("var data = " + JsonUtil.toJson(tmp) + ";")
    pw.close()
  }
}