ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "Mongo2Xml"
  )

val mongoVersion = "4.7.2"

libraryDependencies ++= Seq(
  "org.mongodb.scala" %% "mongo-scala-driver" % mongoVersion,
)
