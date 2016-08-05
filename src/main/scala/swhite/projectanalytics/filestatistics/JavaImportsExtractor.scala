package swhite.projectanalytics.filestatistics

import swhite.projectanalytics.json.JsonUtil
import sys.process._

class JavaImportsExtractor {
  def extractAll(repoDirectory: String, idTransformer: String => String, matchingPatterns: List[String]): List[JavaFileStats] = {
    val grepPattern = "^import\\s([^;]+);$".r
    val builder = new FileSizeExtractor
    val javaFiles = builder.extractAll(repoDirectory, "-name *.java")

    javaFiles map { jf =>
      val fullpath = s"$repoDirectory/${jf.id}"
      println(s"processing $fullpath")
      val cmd = s"grep import $fullpath"
      val grepLines = cmd.lineStream_!.toList
        .filter(l => matchingPatterns.exists(l.contains(_)))
        .flatMap {
          case grepPattern(id) => Some(id)
          case _ => None
        }
      JavaFileStats(idTransformer(jf.id), jf.size, grepLines)
    }
  }
}

object JavaImportsExtractor {
  def main(args: Array[String]): Unit = {
    val builder = new JavaImportsExtractor
    def idTransformer(s: String) = s.substring(1 + s.indexOf("/com/")).dropRight(".java".length).replaceAll("/", ".")
    val allImports = builder.extractAll("/Users/swhite/junk/app-core/dev2", idTransformer, List("navigo", "smartsheet"))
    val rootJson = JsonUtil.toJson(allImports)

    print(rootJson)
    import java.io._
    val pw = new PrintWriter(new File("web/datasets/javaImports.js"))
    pw.write(s"var classes = $rootJson;")
    pw.close()
  }
}