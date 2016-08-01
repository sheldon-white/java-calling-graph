package swhite.projectanalytics.processbuilder

import sys.process._
import java.io.File
import scala.collection.mutable
import swhite.projectanalytics.json.JsonUtil
import com.fasterxml.jackson.annotation.JsonIgnore
case class DirectorySize(size: Int, id: String)

class DirectoryTree(val name: String, var size: Int, var children: List[DirectoryTree]) {
  @JsonIgnore val childrenByName: mutable.Map[String, DirectoryTree] = children.map(c => c.name -> c)(collection.breakOut)

  def addChild(path: String, size: Int): Unit = {
    // println(s"adding $value to $path")
    val parts = path.split("/").toList

    childrenByName.get(parts.head) match {
      case Some(child) =>
        parts.length match {
          case 1 =>
          case _: Int =>
            child.addChild(parts.drop(1).mkString("/"), size)
        }
      case None =>
        parts.length match {
          case 1 =>
            val child = new DirectoryTree(parts.head, size, List())
            childrenByName.put(parts.head, child)
            children = children :+ child
          case _: Int =>
            val child = new DirectoryTree(parts.head, 0, List())
            childrenByName.put(parts.head, child)
            child.addChild(parts.drop(1).mkString("/"), size)
            children = children :+ child
        }
    }
  }
}

object DirectoryTreeTest {
  def main(args: Array[String]): Unit = {
    val root = new DirectoryTree("root", 0, List())
    root.addChild("foo/baz/faz/spaz", 123)
    root.addChild("foo/baz/wubba", 99)

    println(JsonUtil.toJson(root))
  }
}

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
      case directorySizePattern(size, id) if !id.startsWith("./.git") => Some(DirectorySize(size.toInt, id))
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
    val repo = "/Users/swhite/junk/app-core"
    val outputDir = "/Users/swhite/junk"
    val dateStr = "2016-07-01"
    val cmd = s"sh $pwd/bin/java-file-sizes-at-date.sh $repo $dateStr $outputDir"

    println(cmd)
    val output = cmd !!
    val lines = output.split("\n")
    val directorySizeSet = lines flatMap {
      case directorySizePattern(size, id) if !id.startsWith("./.git") => Some(DirectorySize(size.toInt, id))
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
