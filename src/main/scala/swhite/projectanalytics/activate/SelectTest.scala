package swhite.projectanalytics.activate

import swhite.projectanalytics.activate.ProjectContext._
import swhite.projectanalytics.json.JsonUtil

object SelectTest extends App {

  class RickshawDataSet(val name: String, val data: List[RickshawDataPoint])

  transactional {
    val commitsByAuthor = query {
      (entity: LineCountByMonthAndAuthor) =>
        where() select entity orderBy (entity.month, entity.authorEmail)
    }.groupBy(gcr => gcr.authorEmail)

    val tmp = commitsByAuthor map {case (authorEmail, commits) => new RickshawDataSet(authorEmail, commits.map( x => x.toBean))}
    print(JsonUtil.toJson(tmp))
  }
}