package swhite.projectanalytics.filestatistics

import sys.process._

class FileSizeExtractor {
  def extractAll(repoDirectory: String, duTest: String) = {
    val directorySizePattern = "^([0-9]+)\\s+(.+)$".r
    val duOutput = s"find $repoDirectory $duTest" #| "xargs gdu -b" !!
    val lines = duOutput.split("\n")

    lines flatMap {
      case directorySizePattern(size, id)
        if !id.contains(".git/") && id.startsWith(repoDirectory ) =>
        Some(FileSize(id.substring(1 + repoDirectory.length), size.toInt))
      case _ => None
    } toList
  }
}

object FileSizeExtractor {
  def main(args: Array[String]): Unit = {
    val builder = new FileSizeExtractor
    builder.extractAll("/Users/swhite/junk/app-core", "-name *.java") foreach println
  }
}