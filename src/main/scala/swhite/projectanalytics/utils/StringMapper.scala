package swhite.projectanalytics.utils

import scala.io.Source

class StringMapper(filename: String) {
  private val stringMap =
    Source.fromFile(filename)
      .getLines.toArray
      .map(_ split ",")
      .collect({ case Array(k, v) => (k.trim, v.trim) })
      .toMap

  def mapString(target: String): String = {
    for (s <- stringMap.keys) {
      if (target.contains(s)) {
        return stringMap(s)
      }
    }
    target
  }
}

object StringMapper {
  def main(args: Array[String]) = {
    val stringMapper = new StringMapper("dataCleaning/authorMappings.csv")
    assert(stringMapper.mapString("swhite@foo.com") == "sheldon.white@smartsheet.com")
  }
}