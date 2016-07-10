package swhite.projectanalytics.githistory

import java.io.File
import scala.collection.parallel._
import swhite.projectanalytics.utils.DirectoryIterator

object GitHistoryApp {
  def main(args: Array[String]) = {
    val repoDir = "/Users/swhite/projects/app-core"
    val extractor = new GitHistoryExtractor(repoDir)
    val dirIterator = new DirectoryIterator
    def isFile(file: File) = file.isFile && !file.getAbsolutePath.contains("/.git") && !file.getAbsolutePath.contains("/.idea") && !file.getAbsolutePath.contains("/.vagrant")

    val parCollection = dirIterator.walkTree(new File(repoDir), isFile).par
    parCollection.tasksupport = new ForkJoinTaskSupport(new scala.concurrent.forkjoin.ForkJoinPool(20))
    parCollection.foreach(extractor.extractCommits)
    System.exit(0)
  }
}