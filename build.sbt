name := "java-calling-graph"

version := "1.0"

scalaVersion := "2.11.8"

scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-unchecked", "-feature")

val playVersion = "2.4.3"

resolvers ++= Seq(
  "anormcypher" at "http://repo.anormcypher.org/",
  "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/",
  "Bintray" at "http://dl.bintray.com/typesafe/maven-releases/com/typesafe/play/extras/"
)


libraryDependencies ++= Seq(
  "org.apache.bcel" % "bcel" % "5.2",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "com.typesafe.play" %% "play-json" % playVersion,
  "com.typesafe.play" %% "play-ws" % playVersion,
  "com.typesafe.play" %% "play-iteratees" % playVersion,
  "com.typesafe.play.extras" %% "iteratees-extras" % "1.5.0",
  "org.scala-lang.modules" %% "scala-async" % "0.9.2",
  "org.anormcypher" %% "anormcypher" % "0.9.1"
)

Seq(lsSettings :_*)

(LsKeys.tags in LsKeys.lsync) := Seq("anorm", "cypher", "neo4j", "neo")

(externalResolvers in LsKeys.lsync) := Seq(
  "anormcypher resolver" at "http://repo.anormcypher.org")
