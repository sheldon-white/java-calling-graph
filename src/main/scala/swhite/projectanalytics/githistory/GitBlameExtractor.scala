package swhite.projectanalytics.githistory

import java.io.File

import swhite.projectanalytics.json.JsonUtil

import scala.sys.process._
import scala.language.implicitConversions

class GitBlameExtractor(repoDir: String) {
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
          m(normalizeAuthor(author.toLowerCase())) += 1
          lines = lines + 1
        case _ => //println
      }
      Some(new BlameStatistics(projectName + "/" + relativePath, lines, m toMap))
    } catch {
      case e: Throwable => None
    }
  }

  private def normalizeAuthor(author: String) = {
    author match {
      case a if a.contains("@batie") => "brett.batie@smartsheet.com"
      case a if a.contains("bharper") => "brian.harper@smartsheet.com"
      case a if a.contains("creason") => "john.creason@smartsheet.com"
      case a if a.contains("echou@") => "erik.chou@smartsheet.com"
      case a if a.contains("gessel") => "jason.gessel@smartsheet.com"
      case a if a.contains("harkenrider") => "nathan.harkenrider@smartsheet.com"
      case a if a.contains("jervis") => "bob.jervis@smartsheet.com"
      case a if a.contains("pgarimella") => "pradeep.garimella@smartsheet.com"
      case a if a.contains("sgalbraith") => "susan.galbraith@smartsheet.com"
      case a if a.contains("skeem") => "kyan.skeem@smartsheet.com"
      case a if a.contains("swhite") => "sheldon.white@smartsheet.com"
      case _ => author
    }
  }
}

object GitBlameExtractor {
  val authorPattern = "^[0-9a-f]+\\s+\\(<([^)]+)>.+$".r

  def commitHandler(blameStats: BlameStatistics): Unit =
    println(JsonUtil.toJson(blameStats))

  def main(args: Array[String]) = {
    val t1 = System.currentTimeMillis
    val repoDir = "/Users/swhite/projects/app-core"
    val extractor = new GitBlameExtractor(repoDir)
    val blameStatistics = extractor.extractBlame(new File("dev2/web/opscon/jsx/app.jsx"))
    println(blameStatistics)
    val t2 = System.currentTimeMillis
    println(s"elapsed time = ${t2 - t1} milliseconds")
  }
}
