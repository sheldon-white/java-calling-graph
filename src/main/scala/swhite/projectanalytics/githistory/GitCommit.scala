package swhite.projectanalytics.githistory

import org.joda.time.DateTime

class GitCommit(val filename: String, val authorEmail: String, val commitDate: DateTime, linesAdded: Int) {
  override def toString = {
    filename + ": " + authorEmail + ", " + commitDate + ", " + linesAdded
  }
}
