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
    val javapLines = Process(javap + " -c " + classfile).lineStream.filter(l => l.contains("invoke"))

    val calledMethods: List[CalledMethod] = javapLines.map(l =>
      l match {
          case ClassFileDisassembler.invokeVirtualPattern(null, function) => Option(new CalledMethod(function, javaClass.getClassName))
          case ClassFileDisassembler.invokeVirtualPattern(classname, function) => Option(new CalledMethod(function, classname))
          case ClassFileDisassembler.invokeStaticPattern(null, function) =>  Option(new CalledMethod(function, javaClass.getClassName))
          case ClassFileDisassembler.invokeStaticPattern(classname, function) => Option(new CalledMethod(function, classname))
          case _ => None
      }).toList.flatten

    new ClassProfile(javaClass.getClassName, javaClass.getPackageName, calledMethods)
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
  }
}
