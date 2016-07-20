package swhite.projectanalytics.githistory

import java.util.Date

class CommitData(val commitID: String,
                 val filename: String,
                 val authorEmail: String,
                 val linesAdded: Int,
                 val linesDeleted: Int,
                 val commitDate: Date) {
  override def toString = {
    commitDate + ": " + commitID + " - " + filename + ": " + authorEmail + ", " + linesAdded + ", " + linesDeleted
  }
}
