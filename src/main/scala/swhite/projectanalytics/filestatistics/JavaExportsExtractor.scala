package swhite.projectanalytics.filestatistics

import swhite.projectanalytics.json.JsonUtil
import sys.process._

class JavaExportsExtractor {
  def extractAll(repoDirectory: String, matchingPatterns: List[String]): List[JavaFileStats] = {
    val grepPattern = "^import\\s([^;]+);$".r
    val builder = new FileSizeExtractor
    val javaFiles = builder.extractAll(repoDirectory, "-name *.java")

    javaFiles map { jf =>
      val fullpath = s"$repoDirectory/${jf.id}"
      val cmd = s"grep import $fullpath"
      val grepLines = cmd.lineStream_!.toList
          .filter(l => matchingPatterns
          .exists(p => l.contains(p)))
        .flatMap {
          case grepPattern(id) => Some(id)
          case _ => None
        }
      JavaFileStats(jf.id, jf.size, grepLines)
    }
  }
}

object JavaExportsExtractor {
  def main(args: Array[String]): Unit = {
    val builder = new JavaExportsExtractor
    val allImports = builder.extractAll("/Users/swhite/junk/app-core", List("navigo", "smartsheet"))
    val rootJson = JsonUtil.toJson(allImports)
    print(rootJson)
    import java.io._
    val pw = new PrintWriter(new File("web/datasets/javaImports.js"))
    pw.write(s"var data = $rootJson;")
    pw.close()

  }
}