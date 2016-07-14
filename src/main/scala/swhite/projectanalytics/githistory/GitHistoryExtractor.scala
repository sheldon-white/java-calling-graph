package swhite.projectanalytics.githistory

import java.io.File
import java.nio.file.Paths

import scala.sys.process._
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import scala.language.implicitConversions

class GitHistoryExtractor(repoDir: String) {
  val git = "which git".!!.trim
  var filename: String = null
  var commitAuthor: String = null
  var commitID: String = null
  var commitDate: DateTime = null
  var deltaLineCount = 0
  val dateFormatter = DateTimeFormat.forPattern("EEE MMM dd HH:mm:ss yyyy Z")

  implicit def str2date(str: String): DateTime = dateFormatter.parseDateTime(str)

  def extractCommits(file: File, isRelative: Boolean, commitHandler: CommitData => Unit) = {
    val repoPath = Paths.get(repoDir)
    val repoFilePath = isRelative match {
      case true => repoPath.relativize(Paths.get(file.getAbsolutePath))
      case false => file.getName
    }
    val pwd = new File(".").getAbsolutePath
    val gitCmd = s"sh $pwd/bin/run-git.sh $repoDir"

    try {
      gitCmd.lineStream.foreach(l =>
        l match {
          case GitHistoryExtractor.commitIDPattern(id) =>
            if (commitID != null) {
              // finish current commit
              attemptBuildCommit(commitID, filename, commitAuthor, commitDate, deltaLineCount) match {
                case Some(c) =>
                  commitHandler(c)
                case None =>
              }
            }
            resetState()
            commitID = id

          case GitHistoryExtractor.filenamePattern(fn) =>
            // finish current commit
            attemptBuildCommit(commitID, filename, commitAuthor, commitDate, deltaLineCount) match {
              case Some(c) =>
                commitHandler(c)
              case None =>
            }
            deltaLineCount = 0
            filename = fn

          case GitHistoryExtractor.authorPattern(author) =>
            commitAuthor = author

          case GitHistoryExtractor.datePattern(dateStr) =>
            commitDate = dateStr

          case GitHistoryExtractor.linesAddedDeletedPattern(linesDeleted, linesAdded) =>
            deltaLineCount += linesAdded.toInt - linesDeleted.toInt

          case _ =>
        })
    } catch {
      case e: Throwable => Console.err.println(e)
    }
    if (commitID != null) {
      // finish current commit
      attemptBuildCommit(commitID, filename, commitAuthor, commitDate, deltaLineCount) match {
        case Some(c) =>
          commitHandler(c)
          resetState()
        case None =>
      }
    }
  }

  def resetState() = {
    commitID = null
    filename = null
    commitAuthor = null
    commitDate = null
    deltaLineCount = 0
  }

  def attemptBuildCommit(commitID: String,
                         filename: String,
                         commitAuthor: String,
                         commitDate: DateTime,
                         deltaLineCount: Int): Option[CommitData] = {
    if (filename != null && commitID != null && commitAuthor != null && commitDate != null) {
      // println(filename, commitAuthor, commitDate)
      //printStats
      Some(new CommitData(commitID, filename, commitAuthor, deltaLineCount, commitDate.toDate))
    } else {
      Console.err.println(s"commit $commitID was unterminated!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
      None
    }
  }

  def printStats = {
    def rt = Runtime.getRuntime
    def usedMB = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024
    println(s"usage = $usedMB")
  }
}

object GitHistoryExtractor {
  val filenamePattern = "^diff --git a/(.+) b/.+$".r
  val commitIDPattern = "^commit\\s+([0-9a-f]+)$".r
  val authorPattern = "^Author:.+<([^>]+)>$".r
  val datePattern = "^Date:\\s+(.+)$".r
  val linesAddedDeletedPattern = "^@@ \\-(?:\\d+),(\\d+) \\+(?:\\d+),(\\d+) @@.*$".r

  def main(args: Array[String]) = {
    val t1 = System.currentTimeMillis
    val repoDir = "/Users/swhite/projects/app-core"
    val commitHandler: CommitData => Unit = println
    val extractor = new GitHistoryExtractor(repoDir)
    extractor.extractCommits(new File("/Users/swhite/projects/app-core/dev2/web/accessToken.jsp"), true, commitHandler)
    val t2 = System.currentTimeMillis
    println(s"elapsed time = ${t2 - t1} milliseconds")
  }
}
