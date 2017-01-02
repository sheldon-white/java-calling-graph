package swhite.projectanalytics.githistory

import java.io._

import swhite.projectanalytics.json.JsonUtil
import swhite.projectanalytics.utils.{DirectoryIterator, StringMapper}

class GitBlameExtractorApp {}

object GitBlameExtractorApp {
  def isSuitableFile(file: File): Boolean = {
    file match {
      case f if !f.isFile => false
      case f if f.getAbsolutePath.endsWith(".zip") => false
      case f if f.getAbsolutePath.endsWith(".png") => false
      case f if f.getAbsolutePath.endsWith(".class") => false
      case f if f.getAbsolutePath.contains("/.git/") => false
      case _ => true
    }
  }

  val allFileStats = collection.mutable.ListBuffer[BlameStatistics]()

  def commitHandler(blameStats: BlameStatistics): Unit = {
    allFileStats.append()
  }

  def main(args: Array[String]) = {
    val dir = "/Users/swhite/projects/app-core"
    val authorMapper = new StringMapper("dataCleaning/authorMappings.csv")
    val extractor = new GitBlameExtractor(dir, authorMapper)
    val finder = new DirectoryIterator
    val fileStream = finder.walkTree(new File(dir), isSuitableFile)

    for (f <- fileStream) {
      extractor.extractBlame(f) match {
        case Some(b) => allFileStats.append(b)
        case None =>
      }
    }

    val pw = new PrintWriter(new File("web/datasets/blameByFile.json"))
    pw.write(JsonUtil.toJson(allFileStats))
    pw.close()
    System.exit(0)
  }
}