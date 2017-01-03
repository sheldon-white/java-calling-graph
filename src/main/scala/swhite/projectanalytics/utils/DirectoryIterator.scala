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
    val dir = new File("project-analytics")
    val finder = new DirectoryIterator
    val t1 = System.currentTimeMillis()
    for (f <- finder.walkTree(dir, isFile)) {
      println(f.getName, f.length())
    }
    val t2 = System.currentTimeMillis()
    println("Sequential " + (t2 - t1) + " (ms)")
    Thread.sleep(5000)
    val t3 = System.currentTimeMillis()
    val finder2 = new DirectoryIterator
    finder2.walkTree(dir, isFile).par.foreach(f => println(f.getName(), f.length()))
    val t4 = System.currentTimeMillis()
    println("Parallel " + (t4 - t3) + " (ms)")
  }
}


