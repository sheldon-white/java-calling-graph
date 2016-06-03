package swhite.javacallinggraph

import play.api.libs.ws._
import org.anormcypher._

object AnormCypherExample {
  def main(args: Array[String]): Unit = {
    // Setup the Rest Client
    // Need to add the Neo4jConnection type annotation so that the default
    // Neo4jConnection -> Neo4jTransaction conversion is in the implicit scope
    val wsclient = ning.NingWSClient()
    implicit val connection: Neo4jConnection = Neo4jREST("192.168.99.100", 32772, "neo4j", "test123")(wsclient)
    implicit val ec = scala.concurrent.ExecutionContext.global

    // create some test nodes
    Cypher("""create (anorm:anormcyphertest {name:"AnormCypher"}), (test:anormcyphertest {name:"Test"})""").execute()

    // a simple query
    val req = Cypher("match (n:anormcyphertest) return n.name")

    // get a stream of results back
    val stream = req()

    // get the results and put them into a list
    stream.map(row => {
      row[String]("n.name")
    }).toList.foreach(println)

    // remove the test nodes
    //Cypher("match (n:anormcyphertest) delete n")()

    // shut down WSClient
    wsclient.close()
  }
}