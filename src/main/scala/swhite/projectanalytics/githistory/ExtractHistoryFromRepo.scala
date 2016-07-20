package swhite.projectanalytics.githistory

import java.io.File
import java.util.Date
import java.util.concurrent.atomic._

import swhite.projectanalytics.activate.GitCommitRecord
import swhite.projectanalytics.activate.ProjectContext._
import swhite.projectanalytics.utils.DirectoryIterator

import scala.collection.parallel._

object ExtractHistoryFromRepo {
  def commitIt(commitData: CommitData) = {
    val transaction = new Transaction
    transactional(transaction) {
      println(commitData)
      new GitCommitRecord(commitData)
    }
    transaction.commit
  }

  def main(args: Array[String]) = {
    val repoDir = "/Users/swhite/projects/app-core/.git"
    val extractor = new GitHistoryExtractor(repoDir)
    try {
      extractor.extractCommits(new File("."), false, commitIt)
    } catch {
      case e: Exception => println(e)
    }
   // System.exit(0)
  }
}