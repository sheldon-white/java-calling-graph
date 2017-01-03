package swhite.javacallinggraph

import java.io.File

import swhite.projectanalytics.utils.DirectoryIterator

object CallingGraphApp1 {
  def isClassFile(file: File) = {
    file.getAbsolutePath.endsWith(".class")
  }

  def main(args: Array[String]) = {
    val dir = args(0)
    val disassembler = new ClassFileDisassembler
    val dao = new Neo4JDAO
    dao.deleteEverything

    val finder = new DirectoryIterator
    val classStream = finder.walkTree(new File(dir), isClassFile).map(disassembler.extractMetadata)

    println("CS = " + classStream.getClass)

    for (cp <- classStream) {
      println(cp.className + ", ", cp.calledMethods.length)
      cp.calledMethods.foreach(m => dao.recordRelationship(cp.className, m.className, m.methodName))
    }
     // .foreach(cp => cp.calledMethods.foreach(calledFunction => dao.recordRelationship(cp.className, calledFunction.methodName, "calls")))
    System.exit(0)
  }
}