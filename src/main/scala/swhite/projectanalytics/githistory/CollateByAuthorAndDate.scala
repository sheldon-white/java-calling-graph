package swhite.projectanalytics.githistory

import java.io.File

import swhite.projectanalytics.activate.GitCommitRecord
import swhite.projectanalytics.activate.ProjectContext._
import swhite.projectanalytics.utils.StringMapper

object CollateByAuthorAndDate {
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
    val authorMapper = new StringMapper("dataCleaning/authorMappings.csv")
    val extractor = new GitHistoryExtractor(repoDir, authorMapper)
    try {
      extractor.extractCommits(new File("."), false, commitIt)
    } catch {
      case e: Exception => println(e)
    }
   // System.exit(0)
  }
}