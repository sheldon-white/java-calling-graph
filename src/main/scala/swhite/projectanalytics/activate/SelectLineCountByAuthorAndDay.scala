package swhite.projectanalytics.activate

import java.io._

import swhite.projectanalytics.activate.ProjectContext._
import swhite.projectanalytics.json.JsonUtil

object SelectLineCountByAuthorAndDay extends App {
  class CommitData(val commitID: String, val authorEmail: String, val linesAdded: Int)
  class DataSet(val day: String, val commits: List[CommitData])

  transactional {
    val commitsByAuthor = query {
      (entity: LineCountByDayAndAuthor) =>
        where() select entity orderBy (entity.day, entity.commitID, entity.authorEmail)
    }.groupBy(gcr => gcr.day)

    val tmp = commitsByAuthor map {
      case (day, commits) => {
        val chopped = day.split(" ")(0) // cheesy, I know
        new DataSet(chopped, commits.map(c => new CommitData(c.commitID, c.authorEmail, c.linesAdded)))
      }
    }

    val pw = new PrintWriter(new File("web/datasets/commitsByDayAndAuthor.json"))
    pw.write(JsonUtil.toJson(tmp.toSeq.sortBy(_.day)))
    pw.close()
  }
}