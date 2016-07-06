package swhite.projectanalytics.parallelcollections

class Data(val a:Int = 0 , val b:Int = 0)

object Data {
  def apply(n:Int):List[Data] = {
    val r = new util.Random
    val lists = for { i <- 0 to n } yield new Data(r.nextInt(10000), r.nextInt(10000))
    lists.toList
  }
}
