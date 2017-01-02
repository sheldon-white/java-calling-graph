package swhite.projectanalytics.activate

import java.util.Date

import swhite.projectanalytics.githistory.CommitData

class GitCommitRecord(var commitID: String,
                      var filename: String,
                      var authorEmail: String,
                      var linesAdded: Int,
                      var linesDeleted: Int,
                      var commitDate: Date) extends CommitEntity {

  def this(commitData: CommitData) {
    this(commitData.commitID,
         commitData.filename,
         commitData.authorEmail,
         commitData.linesAdded,
         commitData.linesDeleted,
         commitData.commitDate)
  }
}
