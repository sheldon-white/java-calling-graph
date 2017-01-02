package swhite.javacallinggraph

import org.anormcypher._
import play.api.libs.ws._

object AnormCypherExample {
  def main(args: Array[String]): Unit = {
    // Setup the Rest Client
    // Need to add the Neo4jConnection type annotation so that the default
    // Neo4jConnection -> Neo4jTransaction conversion is in the implicit scope
    val wsclient = ning.NingWSClient()
    implicit val connection: Neo4jConnection = Neo4jREST("192.168.99.100", 32772, "neo4j", "test123")(wsclient)
    implicit val ec = scala.concurrent.ExecutionContext.global

    // delete existing nodes and links
    Cypher("""match (n)
      optional match (n)-[r]-()
      delete n, r;
           """)()

    // create some test nodes and links
    Cypher("""create
      (us {type:"Country", name:"United States", code:"USA", tag:"anormcyphertest"}),
      (germany {type:"Country", name:"Germany", code:"DEU", population:81726000, tag:"anormcyphertest"}),
      (france {type:"Country", name:"France", code:"FRA", tag:"anormcyphertest", indepYear:1789}),
      (monaco {name:"Monaco", population:32000, type:"Country", code:"MCO", tag:"anormcyphertest"}),
      (english {type:"Language", name:"English", code:"EN", tag:"anormcyphertest"}),
      (french {type:"Language", name:"French", code:"FR", tag:"anormcyphertest"}),
      (german {type:"Language", name:"German", code:"DE", tag:"anormcyphertest"}),
      (arabic {type:"Language", name:"Arabic", code:"AR", tag:"anormcyphertest"}),
      (italian {type:"Language", name:"Italian", code:"IT", tag:"anormcyphertest"}),
      (russian {type:"Language", name:"Russian", code:"RU", tag:"anormcyphertest"}),
      (france)-[:speaks {official:true}]->(french),
      (france)-[:speaks]->(arabic),
      (france)-[:speaks]->(italian),
      (germany)-[:speaks {official:true}]->(german),
      (germany)-[:speaks]->(english),
      (germany)-[:speaks]->(russian),
      (proptest {name:"proptest", tag:"anormcyphertest", f:1.234, i:1234, l:12345678910, s:"s", arri:[1,2,3,4], arrs:["a","b","c"], arrf:[1.234,2.345,3.456]});
           """)()

    // a simple query
    val stream = Cypher("match (n:anormcyphertest) return n.name")()
   // val stream = req()

    // get the results and put them into a list
    stream.map(row => {
      row[String]("n.name")
    }).toList.foreach(println)

    // shut down WSClient
    wsclient.close()
  }
}