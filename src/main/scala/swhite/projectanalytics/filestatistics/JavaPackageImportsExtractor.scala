package swhite.projectanalytics.filestatistics

import swhite.projectanalytics.json.JsonUtil
import sys.process._
import scala.collection.mutable

class JavaPackageImportsExtractor {
  def extractAll(repoDirectory: String, idTransformer: String => String, matchingPatterns: List[String]): List[JavaFileStats] = {
    val grepPattern = "^import\\s([^;]+);$".r
    val builder = new FileSizeExtractor
    val javaFiles = builder.extractAll(repoDirectory, "-name *.java")
    val processedPackages: mutable.Map[String, JavaFileStats] = mutable.Map()

    // for each java file, extract the package. If we haven't seen it before, grab the imports.
    javaFiles map { jf =>
      val javaPackage = idTransformer(jf.id)
      if (!processedPackages.contains(javaPackage)) {
        val fullpath = s"$repoDirectory/${jf.id}"
        println(s"processing $fullpath")
        val cmd = s"grep import $fullpath"
        val grepLines = cmd.lineStream_!.toList
          .filter(l => matchingPatterns.exists(l.contains(_)))
          .flatMap {
            case grepPattern(id) => Some(id.substring(0, id.lastIndexOf('.')))
            case _ => None
          }
        processedPackages.put(javaPackage, JavaFileStats(idTransformer(jf.id), jf.size, grepLines.distinct))
      }
    }
    processedPackages.values.toList
  }

}

object JavaPackageImportsExtractor {
  def main(args: Array[String]): Unit = {
    val builder = new JavaPackageImportsExtractor
    def idTransformer(s: String) = s.substring(0, s.lastIndexOf('/')).substring(1 + s.indexOf("/com/")).replaceAll("/", ".")

    val allImports = builder.extractAll("/Users/swhite/projects/app-core/dev2", idTransformer, List("navigo", "smartsheet"))
    val rootJson = JsonUtil.toJson(allImports)

    print(rootJson)
    import java.io._
    val pw = new PrintWriter(new File("web/datasets/javaPackageImports.js"))
    pw.write(s"var classes = $rootJson;")
    pw.close()
  }
}