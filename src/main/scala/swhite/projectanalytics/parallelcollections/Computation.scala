package swhite.projectanalytics.parallelcollections

trait Computation {
  def f(data:Data):Int =  {
    Thread.sleep(10)
    data.a + data.b
  }
  def compute(list:List[Data]):Int
}
