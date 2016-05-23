import java.io.File
import scala.sys.process._

class ClassFileDisassembler {
  val javap = "which javap".!!.trim

  def runJavap(file: File): Stream[String] = {
    Process(javap + " -c " + file).lineStream
  }
}

object ClassFileDisassembler {
  def main(args: Array[String]) = {
    val classfile = new File("/Users/swhite/projects/app-core/dev2/target/test-classes/com/navigo/unittest/UserAutoProvisioningTest.class")
    val disassembler = new ClassFileDisassembler()
    for (f <- disassembler.runJavap(classfile)) {
      println(f)
    }
  }
}
