package swhite.projectanalytics.githistory

class BlameStatistics(val filename: String,
                      val lines: Int,
                      val blameByAuthor: Map[String, Int]) {
  override def toString = {
    s"$filename: $lines, $blameByAuthor"
  }
}
