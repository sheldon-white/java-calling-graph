package swhite.projectanalytics.json


object JsonSerializationTest {
  def main(args: Array[String]): Unit = {
    case class Person(name: String, age: Int)
    case class Group(name: String, persons: Seq[Person], leader: Person)

    val jeroen = Person("Jeroen", 26)
    val martin = Person("Martin", 54)

    val originalGroup = Group("Scala ppl", Seq(jeroen, martin), martin)
    val personMap = Map("people" -> List(jeroen, martin))
    // originalGroup: Group = Group(Scala ppl,List(Person(Jeroen,26), Person(Martin,54)),Person(Martin,54))
    println(JsonUtil.toJson(personMap))
    val groupJson = JsonUtil.toJson(originalGroup)
    println(groupJson)
    // groupJson: String = {"name":"Scala ppl","persons":[{"name":"Jeroen","age":26},{"name":"Martin","age":54}],"leader":{"name":"Martin","age":54}}
  }
}