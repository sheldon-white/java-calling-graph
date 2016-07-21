import sbt.Keys._
import sbt._

object ProjectBuild extends Build {

	/* Dependencies */
	val localMavenRepo = "Local Maven Repository" at "file://"+Path.userHome+"/.m2/repository"
	val fwbrasilRepo = "fwbrasil.net" at "http://fwbrasil.net/maven/"

	val activateVersion = "1.7"
	val playVersion = "2.4.3"
	val activateCore = "net.fwbrasil" %% "activate-core" % activateVersion
	val activateJdbc = "net.fwbrasil" %% "activate-jdbc" % activateVersion
	val mysql = "mysql" % "mysql-connector-java" % "5.1.16"

	lazy val project =
		Project(
			id = "project-analytics",
			base = file("."),
			settings = Defaults.coreDefaultSettings ++ Seq(
				/* ADD THE DEPENDENCY TO YOUR STORAGE HERE */
				libraryDependencies ++=
					Seq("org.apache.bcel" % "bcel" % "5.2",
						"org.scalatest" %% "scalatest" % "2.2.4" % "test",
						"com.typesafe.play" %% "play-json" % playVersion,
						"com.typesafe.play" %% "play-ws" % playVersion,
						"com.typesafe.play" %% "play-iteratees" % playVersion,
						"com.typesafe.play.extras" %% "iteratees-extras" % "1.5.0",
						"org.scala-lang.modules" %% "scala-async" % "0.9.2",
						"joda-time" % "joda-time" % "2.9.4",
						"org.anormcypher" %% "anormcypher" % "0.9.1",
            "com.fasterxml.jackson.module" % "jackson-module-scala_2.11" % "2.8.0.rc2",
						activateCore,
						activateJdbc,
						mysql),
				organization := "swhite.projectanalytics",
				scalaVersion := "2.11.8",
				version := "1.0",
				resolvers ++= Seq(localMavenRepo, fwbrasilRepo)))
}