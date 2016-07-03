package swhite.javacallinggraph

import java.io.File

class ClassFileFinder {
  def walkTree(file: File, tester: (String) => Boolean): Stream[File] = {
    val children = new Iterable[File] {
      def iterator = if (file.isDirectory)
        file.listFiles.iterator
      else
        Iterator.empty
    }
    (Seq(file) ++: children.flatMap(walkTree(_, tester))).filter(f => tester(f.getPath)).toStream
  }
}

object ClassFileFinder {
  def isClassFile(filename: String) = {
    filename.endsWith(".class")
  }
  def main(args: Array[String]) = {
    val dir = new File("/Users/swhite/projects/java-calling-graph")
    val finder = new ClassFileFinder
    for (f <- finder.walkTree(dir, isClassFile)) {
      println(f.getName)
    }
  }
}
