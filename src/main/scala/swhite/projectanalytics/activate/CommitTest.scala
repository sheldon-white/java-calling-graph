package swhite.projectanalytics.activate

import swhite.projectanalytics.githistory.CommitData
import swhite.projectanalytics.activate.projectContext._
import net.fwbrasil.radon.transaction.Transaction
import java.util.Date


object CommitTestMain extends App {

  // Use whenever entities within transactions
  // It is not necessary to call a method like "store" or "save" to add the entity.
  // Just create, use, and it will be persisted.
  transactional {
    val commitData = new CommitData("foo.java", "bill@ding.com", 123, new Date())
    val commitRecord = new GitCommitRecord(commitData)
    commitRecord.filename = "baz.js"
    println(commitRecord.filename)
  }

  // Queries
  // The query operators available are :==, :<, :>, :<=, :>=, isNone, isSome, :||, :&&, like and matches.
  // Note that the queries can be made about abstract entities (traits and classes).
  // Perform queries within transactions
  transactional {
    val result = query {
      (commitRecord: GitCommitRecord) => where(commitRecord.filename :== "baz.js") select commitRecord
    }
    for (commitRecord <- result)
      println(commitRecord.filename)
  }

  // There are alternative forms of query.
  // With the "select where" you can use a list of criterias.
  transactional {
    val personList1 = all[GitCommitRecord]
    val personList2 = select[GitCommitRecord] where (_.filename :== "baz.js", _.authorEmail :== "bill@ding.com")
    println(personList1, personList2)
  }

  // To delete an entity
  transactional {
    for (commitRecord <- all[GitCommitRecord])
      commitRecord.delete
  }

  // Typically transactional blocks are controlled by the framework.
  // But you can control the transaction as follows
  val transaction = new Transaction
  transactional(transaction) {
    new GitCommitRecord("foo.java", "fred@flintstone.com", 456, new Date())
  }
  transaction.commit

  // Defining the propagation of the transaction
  transactional {
    val commitRecord = new GitCommitRecord("foo.java", "fred@flintstone.com", -123, new Date())
    transactional(mandatory) {
      commitRecord.filename = "baz2.js"
    }
    println(commitRecord.filename)
  }

  // Nested transactions are a type of propagation
  transactional {
    val commitRecord = new GitCommitRecord("foo.java", "fred@flintstone.com", 192, new Date())
    transactional(nested) {
      commitRecord.filename = "baz3.js"
    }
    println(commitRecord.filename)
  }

  // Activate supports mass update/delete statements.
  // Use them when you have to perform a really big update/delete operation.
  transactional {
    update {
      (commitRecord: GitCommitRecord) => where(commitRecord.filename :== "foo.java") set (commitRecord.filename := "baz.js")
    }
    delete {
      (commitRecord: GitCommitRecord) => where(commitRecord.filename :== "baz.js")
    }
  }
}