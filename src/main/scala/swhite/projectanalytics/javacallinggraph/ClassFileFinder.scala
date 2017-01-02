package swhite.projectanalytics.javacallinggraph

import java.io.File

import swhite.projectanalytics.utils.DirectoryIterator

object ClassFileFinder {
  def isClassFile(file: File) = {
    file.getPath.endsWith(".class")
  }

  def main(args: Array[String]) = {
    val dir = new File("/Users/swhite/projects/project-analytics")
    val finder = new DirectoryIterator
    for (f <- finder.walkTree(dir, isClassFile)) {
      println(f.getName)
    }
  }
}
