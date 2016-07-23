package swhite.projectanalytics.activate

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import swhite.projectanalytics.activate.ProjectContext._

class LineCountByMonthAndAuthor(val id: Int,
                                val month: String,
                                val authorEmail: String,
                                val linesAdded: Int) extends EntityWithCustomID[Int] {

  def dateFormatter = DateTimeFormat.forPattern("yyyy-MM")
  def str2date(str: String): DateTime = dateFormatter.parseDateTime(str)

  def toBean = {
    val timestamp = str2date(month).toDate.getTime / 1000
    new GraphPoint(timestamp, linesAdded)
  }
}

object LineCountByMonthAndAuthor {
  def empty = new LineCountByMonthAndAuthor(0, null, null, 0)
}
