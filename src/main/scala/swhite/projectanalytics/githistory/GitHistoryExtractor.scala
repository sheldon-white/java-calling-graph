package swhite.projectanalytics.githistory

import java.io.File
import java.nio.file.Paths
import scala.collection.mutable.ArrayBuffer

import scala.sys.process._
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import scala.language.implicitConversions

class GitHistoryExtractor(repoDir: String) {
  val git = "which git".!!.trim
  var commitAuthor: String = null
  var commitDate: DateTime = null
  var lineCountsSeen = false
  var deltaLineCount = 0
  val commits = ArrayBuffer.empty[GitCommit]
  val dateFormatter = DateTimeFormat.forPattern("EEE MMM dd HH:mm:ss yyyy Z")

  implicit def str2date(str: String): DateTime = dateFormatter.parseDateTime(str)

  def extractCommits(file: File): ArrayBuffer[GitCommit] = {
    val repoPath = Paths.get(repoDir)
    val filePath = Paths.get(file.getAbsolutePath)
    val fileSubpath = repoPath.relativize(filePath)
    val gitCmd = git + " --git-dir=" + repoDir + "/.git --no-pager log --follow -p -- " + fileSubpath
    //println(gitCmd)
    try {
      val gitOutput = Process(gitCmd).lineStream
      println(s"processing '$file'")

      gitOutput.foreach(l =>
        l match {
          case GitHistoryExtractor.authorPattern(author) => {
            commitAuthor = author
            attemptBuildCommit(file.getAbsolutePath, commitAuthor, commitDate, deltaLineCount) match {
              case Some(c) =>
                commits += c
                resetState
              case None =>
            }
          }
          case GitHistoryExtractor.datePattern(dateStr) => {
            commitDate = dateStr
            attemptBuildCommit(file.getAbsolutePath, commitAuthor, commitDate, deltaLineCount) match {
              case Some(c) =>
                commits += c
                resetState
              case None =>
            }
          }
          case GitHistoryExtractor.linesAddedDeletedPattern(linesDeleted, linesAdded) => {
            deltaLineCount = linesAdded.toInt - linesDeleted.toInt
            lineCountsSeen = true
            attemptBuildCommit(file.getAbsolutePath, commitAuthor, commitDate, deltaLineCount) match {
              case Some(c) =>
                commits += c
                resetState
              case None =>
            }
          }
          case _ =>
        })
    } catch {
      case e: Throwable => Console.err.println(e)
    }

    commits
  }

  def resetState = {
    commitAuthor = null
    commitDate = null
    lineCountsSeen = false
    deltaLineCount = 0
  }

  def attemptBuildCommit(filename: String,
                         commitAuthor: String,
                         commitDate: DateTime,
                         deltaLineCount: Int): Option[GitCommit] = {
    if (commitAuthor != null && commitDate != null && lineCountsSeen) {
      println(filename, commitAuthor, commitDate)
      Some(new GitCommit(filename, commitAuthor, commitDate, deltaLineCount))
    } else {
      None
    }
  }
}

object GitHistoryExtractor {
  val authorPattern = "^Author:.+<([^>]+)>$".r
  val datePattern = "^Date:\\s+(.+)$".r
  val linesAddedDeletedPattern = "^@@ \\-(?:\\d+),(\\d+) \\+(?:\\d+),(\\d+) @@ .+$".r

  def main(args: Array[String]) = {
    val repoDir = "/Users/swhite/projects/app-core"
    val extractor = new GitHistoryExtractor(repoDir)
    extractor.extractCommits(new File("/Users/swhite/projects/app-core/Vagrantfile"))
    //println(classProfile.packageName, classProfile.className)
  }
}
