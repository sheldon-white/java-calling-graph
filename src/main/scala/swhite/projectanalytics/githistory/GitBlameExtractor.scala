package swhite.projectanalytics.githistory

import java.io.File

import swhite.projectanalytics.json.JsonUtil
import swhite.projectanalytics.utils.StringMapper

import scala.language.implicitConversions
import scala.sys.process._

class GitBlameExtractor(repoDir: String, private val authorMapper: StringMapper) {
  val git: String = "which git".!!.trim
  val projectName: String = new File(repoDir).getName
  def extractBlame(file: File): Option[BlameStatistics] = {
    val relativePath = file.toString match {
      case s if s.startsWith(repoDir + "/") => s.substring(repoDir.length + 1)
      case _ => file.toString
    }
    val gitCmd = s"$git --git-dir=$repoDir/.git --work-tree=$repoDir --no-pager blame -c -e $relativePath"
    println(gitCmd)
    val m = collection.mutable.Map[String, Int]().withDefaultValue(0)
    var lines = 0
    try {
      gitCmd.lineStream.foreach {
        case GitBlameExtractor.authorPattern(author) =>
          m(authorMapper.mapString(author.toLowerCase)) += 1
          lines = lines + 1
        case _ => //println
      }
      Some(new BlameStatistics(projectName + "/" + relativePath, lines, m toMap))
    } catch {
      case e: Throwable => None
    }
  }
}

object GitBlameExtractor {
  val authorPattern = "^[0-9a-f]+\\s+\\(<([^)]+)>.+$".r

  def commitHandler(blameStats: BlameStatistics): Unit =
    println(JsonUtil.toJson(blameStats))

  def main(args: Array[String]): Unit = {
    val t1 = System.currentTimeMillis
    val repoDir = "/Users/swhite/projects/app-core"
    val authorMapper = new StringMapper("dataCleaning/authorMappings.csv")
    val extractor = new GitBlameExtractor(repoDir, authorMapper)
    val blameStatistics = extractor.extractBlame(new File("dev2/web/opscon/jsx/app.jsx"))
    println(blameStatistics)
    val t2 = System.currentTimeMillis
    println(s"elapsed time = ${t2 - t1} milliseconds")
  }
}
