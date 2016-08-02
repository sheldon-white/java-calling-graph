package swhite.projectanalytics.processbuilder

import sys.process._
import swhite.projectanalytics.json.JsonUtil

class FileSizeTreeBuilder {
  def build(repoDirectory: String, duTest: String): DirectoryTree = {
    val directorySizePattern = "^([0-9]+)\\s+(.+)$".r
    val duOutput = s"find $repoDirectory $duTest" #| "xargs gdu -b" !!
    val lines = duOutput.split("\n")

    val directorySizeSet = lines flatMap {
      case directorySizePattern(size, id)
        if !id.contains(".git/") && id.startsWith(repoDirectory ) =>
          Some(DirectorySize(size.toInt, id.substring(1 + repoDirectory.length)))
      case _ => None
    } toList
    val root = new DirectoryTree("app-core", 0, List())
    for (cd <- directorySizeSet) {
      root.addChild(cd.id, cd.size)
    }
    root
  }
}

object FileSizeTreeBuilder {
  def main(args: Array[String]): Unit = {
    val builder = new FileSizeTreeBuilder
    val root = builder.build("/Users/swhite/junk/app-core", "-name *.java")
    val rootJson = JsonUtil.toJson(root)
    print(rootJson)
    import java.io._
    val pw = new PrintWriter(new File("web/datasets/javaFileSizes.js"))
    pw.write(s"var data = $rootJson;")
    pw.close()
  }
}