package swhite.projectanalytics.processbuilder

import sys.process._
import java.io.File

import swhite.projectanalytics.json.JsonUtil

import scala.collection.mutable.ListBuffer

case class CircleData(val value: Int, val id: String)
class SizesAtDate(val dateStr: String, val sizes: List[CircleData])

object ProcessBuilderTest {
  def main(args: Array[String]): Unit = {
    val circleArgsPattern = "^([0-9]+)\\s+(.+)$".r
    val pwd = new File(".").getAbsolutePath
    val repo = "/Users/swhite/junk/app-core"
    val outputDir = "/Users/swhite/junk"
    val allMonthData: ListBuffer[SizesAtDate] = ListBuffer()
    //for (dateStr <- nextMonth) {
    nextMonth.foreach { dateStr =>
      val cmd = s"sh $pwd/bin/checkout-version-at-date.sh $repo $dateStr $outputDir"
      println(cmd)
      val output = cmd !!
      val lines = output.split("\n")
      val circleDataSet = lines flatMap {
          case circleArgsPattern(value, id) if !id.startsWith("./.git") => Some(CircleData(value.toInt, id))
          case _ => None
      } toList

      allMonthData += new SizesAtDate(dateStr, circleDataSet)
    }

    import java.io._
    val pw = new PrintWriter(new File("web/datasets/directorySizesByMonth.js"))
    pw.write("var data = " + JsonUtil.toJson(allMonthData.toList) + ";")
    pw.close()
  }

  val nextMonth = for (y <- 2008 to 2016; m <- 1 to 12) yield s"$y-${"%02d".format(m)}"
}
