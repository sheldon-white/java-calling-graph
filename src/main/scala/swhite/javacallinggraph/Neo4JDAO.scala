package swhite.javacallinggraph

import org.anormcypher._
import play.api.libs.ws._

class Neo4JDAO {
  val wsclient = ning.NingWSClient()
  implicit val connection: Neo4jConnection = Neo4jREST("192.168.99.100", 32769, "neo4j", "test123")(wsclient)
  implicit val ec = scala.concurrent.ExecutionContext.global

  def deleteEverything = {
    // delete existing nodes and links
    Cypher(
      """match (n)
      optional match (n)-[r]-()
      delete n, r;
      """)()
  }

  def recordRelationship(caller: String, called: String, relation: String) = {
    val merge =
      "MERGE (c1:Class {name: \"" + caller + "\"}) ON CREATE SET c1.name = \"" + caller + "\" MERGE (c2:Class {name: \"" + called + "\"}) ON CREATE SET c2.name = \"" + called + "\" CREATE UNIQUE (c1) <- [:" + relation + "] - (c2)"

    println(merge)
    Cypher(merge)()
  }
}