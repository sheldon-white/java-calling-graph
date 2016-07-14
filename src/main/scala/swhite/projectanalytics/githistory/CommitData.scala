package swhite.projectanalytics.githistory

import java.util.Date

class CommitData(val commitID: String,
                 val filename: String,
                 val authorEmail: String,
                 val linesAdded: Int,
                 val commitDate: Date) {
  override def toString = {
    commitID + " - " + filename + ": " + authorEmail + ", " + commitDate + ", " + linesAdded
  }
}
