name := "scala.orientdb"

version := "0.1-SNAPSHOT"

scalaVersion := "2.10.0-RC2"

resolvers += Resolver.sonatypeRepo("releases")

libraryDependencies += "org.scalatest" % "scalatest_2.10.0-RC2" % "1.8" % "test"

libraryDependencies += "com.orientechnologies" % "orientdb-core" % "1.2.0"
