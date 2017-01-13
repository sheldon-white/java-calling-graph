package swhite.projectanalytics.activate

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import swhite.projectanalytics.activate.ProjectContext._

class LineCountByDayAndAuthor(val id: Int,
                              val day: String,
                              val commitID: String,
                              val authorEmail: String,
                              val linesAdded: Int) extends EntityWithCustomID[Int] {

  def dateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd")
  def str2date(str: String): DateTime = dateFormatter.parseDateTime(str)

//  def toBean = {
//    val timestamp = str2date(day).toDate.getTime / 1000
//    new GraphPoint(timestamp, linesAdded)
//  }
}

object LineCountByDayAndAuthor {
  def empty = new LineCountByDayAndAuthor(0, null, null, null, 0)
}
