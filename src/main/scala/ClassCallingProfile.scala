import java.io.File

class ClassCallingProfile {
  var className: String = null
  var packageName: String = null

  def buildFrom(classfile: File) = {
    val profile = new ClassCallingProfile
    profile.populateFrom(classfile)
    profile
  }

  private def populateFrom(classfile: File) = {
    val classnamePattern = "public\\s+class\\s+(\\S+).+".r

    val stream = new ClassFileDisassembler().runJavap(classfile)
    for (s <- stream) {
      s match {
        case classnamePattern(classname) => println("class = %s".format(classname))
        case _ =>
      }
    }
  }
}

object ClassCallingProfile {
  def main(args: Array[String]): Unit = {
    val classfile = new File("/Users/swhite/projects/app-core/dev2/target/test-classes/com/navigo/unittest/UserAutoProvisioningTest.class")
    val profile = new ClassCallingProfile
    profile.buildFrom(classfile)
  }
}
