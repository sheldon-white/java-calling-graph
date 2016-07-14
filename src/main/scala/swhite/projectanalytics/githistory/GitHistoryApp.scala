package swhite.projectanalytics.githistory

import java.io.File
import scala.collection.parallel._
import swhite.projectanalytics.utils.DirectoryIterator
import swhite.projectanalytics.activate.GitCommitRecord
import swhite.projectanalytics.activate.ProjectContext._
import java.util.concurrent.atomic._

object GitHistoryApp {
  var progressCtr: AtomicInteger = new AtomicInteger(0)

  def commitIt(commitData: CommitData) = {
    transactional {
      val commitRecord = new GitCommitRecord(commitData)
    }
  }
  def main(args: Array[String]) = {
    val repoDir = "/Users/swhite/projects/app-core"
    val extractor = new GitHistoryExtractor(repoDir)
    val dirIterator = new DirectoryIterator
    def isFile(file: File) = file.isFile && !file.getAbsolutePath.contains("/.git") && !file.getAbsolutePath.contains("/.class") && !file.getAbsolutePath.contains("/.idea") && !file.getAbsolutePath.contains("/.vagrant")

    val parCollection = dirIterator.walkTree(new File(repoDir), isFile).par
    println(s"found ${parCollection.size} files")
    parCollection.tasksupport = new ForkJoinTaskSupport(new scala.concurrent.forkjoin.ForkJoinPool(20))
    parCollection.foreach(f => {
      val ctr = progressCtr.incrementAndGet()
      println(s"$ctr: $f")
      extractor.extractCommits(f, true, commitIt)
    })
    System.exit(0)
  }
}