package swhite.projectanalytics.githistory

import java.io.File

import swhite.projectanalytics.utils.DirectoryIterator

object GitHistoryApp {
  def main(args: Array[String]) = {
    val repoDir = "/Users/swhite/projects/app-core"
    val extractor = new GitHistoryExtractor(repoDir)
    val dirIterator = new DirectoryIterator
    def isFile(file: File) = file.isFile && !file.getAbsolutePath.contains("/.git") && !file.getAbsolutePath.contains("/.idea") && !file.getAbsolutePath.contains("/.vagrant")

    val gitStream = dirIterator.walkTree(new File(repoDir), isFile).map(extractor.extractCommits)

//    println("CS = " + classStream.getClass)

    for (gs <- gitStream) {
      println(gs.toString)
    }
    System.exit(0)
  }
}