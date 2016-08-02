package swhite.projectanalytics.processbuilder

import com.fasterxml.jackson.annotation.JsonIgnore
import scala.collection.mutable
import swhite.projectanalytics.json.JsonUtil

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

