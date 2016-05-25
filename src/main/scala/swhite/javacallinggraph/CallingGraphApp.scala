package swhite.javacallinggraph

import java.io.File

object CallingGraphApp1 {
  def main(args: Array[String]) = {
    val dir = new File("/Users/swhite/projects/app-core/dev2")
    val disassembler = new ClassFileDisassembler
    val finder = new ClassFileFinder
    val classProfiles = finder.walkTree(dir).map(disassembler.extractMetadata)
    println(classProfiles.size)
  }
}