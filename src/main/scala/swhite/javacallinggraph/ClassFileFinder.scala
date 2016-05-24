package swhite.javacallinggraph

import java.io.File

class ClassFileFinder {
  def walkTree(file: File): Iterable[File] = {
    val children = new Iterable[File] {
      def iterator = if (file.isDirectory) file.listFiles.iterator else Iterator.empty
    }
    Seq(file) ++: children.flatMap(walkTree).filter(f => f.getName.endsWith(".class"))
  }
}

object ClassFileFinder {
  def main(args: Array[String]) = {
    val dir = new File("/Users/swhite/projects/java-calling-graph")
    val finder = new ClassFileFinder()
    for (f <- finder.walkTree(dir)) {println(f)
    }
  }
}
