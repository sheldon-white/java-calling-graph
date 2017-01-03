package swhite.projectanalytics.utils

import scala.io.Source

class StringMapper(filename: String) {
  private val matchMap = new collection.mutable.HashMap[String, String]()
  private val stringMap =
    Source.fromFile(filename)
      .getLines.toArray
      .map(_ split ",")
      .collect({ case Array(k, v) => (k.trim, v.trim) })
      .toMap

  def mapString(target: String): String = {
    matchMap.get(target) match {
      case Some(s) => return s
      case None =>
    }

    for (s <- stringMap.keys) {
      if (target.contains(s)) {
        matchMap.put(target, stringMap(s))
        return stringMap(s)
      }
    }
    matchMap.put(target, target)
    target
  }
}

object StringMapper {
  def main(args: Array[String]) = {
    val stringMapper = new StringMapper("dataCleaning/authorMappings.csv")
    assert(stringMapper.mapString("swhite@foo.com") == "sheldon.white@smartsheet.com")
  }
}