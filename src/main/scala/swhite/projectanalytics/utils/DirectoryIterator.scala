package swhite.projectanalytics.utils

import java.io.File

class DirectoryIterator {
  def walkTree(file: File, tester: (File) => Boolean): Stream[File] = {
    val children = new Iterable[File] {
      def iterator = if (file.isDirectory)
        file.listFiles.iterator
      else
        Iterator.empty
    }
    (Seq(file) ++: children.flatMap(walkTree(_, tester))).filter(f => tester(f)).toStream
  }
}

object DirectoryIterator {
  def main(args: Array[String]) = {
    def isFile(file: File) = file.isFile
    val dir = new File("/Users/swhite/projects/app-core")
    val finder = new DirectoryIterator
    var count = 0
    for (f <- finder.walkTree(dir, isFile)) {
      println(f.getName)
      count += 1
    }
    println(s"found $count files")
  }
}


