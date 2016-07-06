package swhite.projectanalytics.parallelcollections

object SequentialImplementation extends Computation {
  def compute(list:List[Data]):Int = list.map{f}.max
}

object ParallelImplementation extends Computation {
  def compute(list:List[Data]):Int = list.par.map{f}.par.max
}

object ParallelExperiment {
  def main(args: Array[String]) {
    val list = Data(args(0).toInt)
    var t1 = System.currentTimeMillis()
    SequentialImplementation.compute(list)
    var t2 = System.currentTimeMillis()
    println("Sequential " + (t2 - t1) + " (ms)")
    t1 = System.currentTimeMillis()
    ParallelImplementation.compute(list)
    t2 = System.currentTimeMillis()
    println("Parallel " + (t2 - t1) + " (ms)")
  }
}