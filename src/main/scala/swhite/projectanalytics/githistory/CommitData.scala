package swhite.projectanalytics.githistory

import java.util.Date

class CommitData(var filename: String,
                 var authorEmail: String,
                 var linesAdded: Int,
                 var commitDate: Date) {
  override def toString = {
    filename + ": " + authorEmail + ", " + commitDate + ", " + linesAdded
  }
}
