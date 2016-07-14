package swhite.projectanalytics.processbuilder

import sys.process._
import java.io.File

object ProcessBuilderTest {
  def main(args: Array[String]): Unit = {
    val pwd = new File(".").getAbsolutePath
    s"sh $pwd/bin/run-git.sh" !
  }
}
