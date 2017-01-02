package swhite.projectanalytics.filestatistics

import java.io.File

import swhite.projectanalytics.json.JsonUtil

import scala.sys.process._

object GitVersionToDirectorySizes {
  def main(args: Array[String]): Unit = {
    val directorySizePattern = "^([0-9]+)\\s+(.+)$".r
    val pwd = new File(".").getAbsolutePath
    val repo = "/Users/swhite/junk/app-core"
    val outputDir = "/Users/swhite/junk"
    val dateStr = "2016-07-01"
    val cmd = s"sh $pwd/bin/directory-sizes-at-date.sh $repo $dateStr $outputDir"

    println(cmd)
    val output = cmd !!
    val lines = output.split("\n")
    val directorySizeSet = lines flatMap {
      case directorySizePattern(size, id) if !id.startsWith("./.git") => Some(FileSize(id, size.toInt))
      case _ => None
    } toList
    val root = new DirectoryTree("app-core", 0, List())
    for (cd <- directorySizeSet) {
      root.addChild(cd.id, cd.size)
    }

    import java.io._
    val pw = new PrintWriter(new File("web/datasets/directorySizes.js"))
    pw.write("var data = " + JsonUtil.toJson(root) + ";")
    pw.close()
  }

  val nextMonth = for (y <- 2008 to 2016; m <- 1 to 12) yield s"$y-${"%02d".format(m)}"
}

object GitVersionToJavaFileSizes {
  def main(args: Array[String]): Unit = {
    val directorySizePattern = "^([0-9]+)\\s+(.+)$".r
    val pwd = new File(".").getAbsolutePath
    val repo = "/Users/swhite/projects/app-core"
    val outputDir = "/Users/swhite/junk"
    //    val dateStr = "2016-07-01"
    //    val cmd = s"sh $pwd/bin/java-file-sizes-at-date.sh $repo $dateStr $outputDir"
    //    println(cmd)
    //    val output = cmd !!
    //    val lines = output.split("\n")

    val duOutput = s"find $repo -name *.java" #| "xargs gdu -b" !!
    val lines = duOutput.split("\n")

    val directorySizeSet = lines flatMap {
      case directorySizePattern(size, id) if !id.contains(".git/") => Some(FileSize(id, size.toInt))
      case _ => None
    } toList
    val root = new DirectoryTree("app-core", 0, List())
    for (cd <- directorySizeSet) {
      root.addChild(cd.id, cd.size)
    }

    import java.io._
    val pw = new PrintWriter(new File("web/datasets/javaFileSizes.js"))
    pw.write("var data = " + JsonUtil.toJson(root) + ";")
    pw.close()
  }

  val nextMonth = for (y <- 2008 to 2016; m <- 1 to 12) yield s"$y-${"%02d".format(m)}"
}
