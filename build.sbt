name := "scala.orientdb"

version := "0.1-SNAPSHOT"

scalaVersion := "2.10.0"

resolvers += Resolver.sonatypeRepo("releases")

libraryDependencies <+= (scalaVersion)("org.scala-lang" % "scala-reflect" % _)

//libraryDependencies += "org.scala-lang" % "scala-reflect" % "2.10.0"

libraryDependencies += "org.scalatest" % "scalatest_2.10.0" % "1.8" % "test"

libraryDependencies += "com.orientechnologies" % "orientdb-core" % "1.3.0"

libraryDependencies += "com.orientechnologies" % "orientdb-client" % "1.3.0"

libraryDependencies += "com.orientechnologies" % "orientdb-server" % "1.3.0"

libraryDependencies += "com.fasterxml.jackson.module" % "jackson-module-scala" % "2.1.2"

libraryDependencies += "net.liftweb" % "lift-json_2.10" % "2.5-RC1"
