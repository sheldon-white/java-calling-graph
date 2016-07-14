package swhite.projectanalytics.activate

import java.util.Date

import net.fwbrasil.radon.transaction.Transaction
import swhite.projectanalytics.activate.ProjectContext._
import swhite.projectanalytics.githistory.CommitData


object CommitTest2 extends App {

  // Use whenever entities within transactions
  // It is not necessary to call a method like "store" or "save" to add the entity.
  // Just create, use, and it will be persisted.
  (1 to 10000).foreach( _ =>
    transactional {
      val commitData = new CommitData("kjbdkjbsdcbs", "foo.java", "bill@ding.com", 123, new Date())
      val commitRecord = new GitCommitRecord(commitData)
      commitRecord.filename = "baz.js"
      println(commitRecord.filename)
    })
}