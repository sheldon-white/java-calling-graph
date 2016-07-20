package swhite.projectanalytics.activate

import swhite.projectanalytics.activate.ProjectContext._

class LineCountByMonthAndAuthor(val id: Int,
                                val month: String,
                                val authorEmail: String,
                                val linesAdded: Int,
                                val linesDeleted: Int) extends EntityWithCustomID[Int] {

  def toBean = {
      new LineCountByMonthAndAuthorBean(id, month, authorEmail, linesAdded, linesDeleted)
  }
}

