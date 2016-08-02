package swhite.projectanalytics.filestatistics

import swhite.projectanalytics.json.JsonUtil

class FileSizeTreeBuilder {
  def build(repoDirectory: String, duTest: String): DirectoryTree = {
    val extractor = new FileSizeExtractor()
    val directorySizeSet =  extractor.extractAll(repoDirectory, duTest)
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