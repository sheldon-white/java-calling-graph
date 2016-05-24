package swhite.javacallinggraph

import java.io.File
import java.io.FileInputStream
import org.apache.bcel.classfile.ClassParser

import scala.sys.process._

class ClassFileDisassembler {
  val javap = "which javap".!!.trim

  def extractMetadata(classfile: File) = {
    val parser = new ClassParser(new FileInputStream(classfile), classfile.getName)
    val javaClass = parser.parse()
    val classProfile = new ClassProfile(javaClass.getClassName, javaClass.getPackageName)

    val javapLines = Process(javap + " -c " + classfile).lineStream.filter(l => l.contains("invoke"))

    for (l <- javapLines) {
      //println(l)
      l match {
        case ClassFileDisassembler.invokeVirtualPattern(null, function) => println("invokevirtual1!!!!!!!!!!!!!!!!!!!!! = %s".format(function))
        case ClassFileDisassembler.invokeVirtualPattern(classname, function) => println("invokevirtual2 = %s, %s".format(classname, function))
        case ClassFileDisassembler.invokeStaticPattern(null, function) => println("invokestatic1 = %s".format(function))
        case ClassFileDisassembler.invokeStaticPattern(classname, function) => println("invokestatic2 = %s, %s".format(classname, function))
        case _ => println(l)
      }
    }

    classProfile
  }
}

object ClassFileDisassembler {
  val invokeVirtualPattern = ".+invokevirtual.+Method ([^\\.]+\\.)?([^:]+).+".r
  val invokeStaticPattern = ".+invokestatic.+Method ([^\\.]+\\.)?([^:]+).+".r

  def main(args: Array[String]) = {
    val classfile = new File("/Users/swhite/projects/app-core/dev2/target/test-classes/com/navigo/unittest/UserAutoProvisioningTest.class")
    val disassembler = new ClassFileDisassembler()
    val classProfile = disassembler.extractMetadata(classfile)
    println(classProfile.packageName)
    //    for (f <- disassembler.runJavap(classfile)) {
    //      println(f)
    //    }
  }
}
