import sbt.Keys._
import sbt._

lazy val commonSettings = Seq(
  version := "0.1.0-SNAPSHOT",
  organization := "io.github.manuzhang",
  scalaVersion := "2.11.8",
  publishMavenStyle := true,
  publishArtifact in Test := false
)

val gearpumpVersion = "0.8.4"
val codahaleVersion = "3.0.2"

lazy val core = (project in file("core")).
  settings(commonSettings: _*).
  settings(
    name := "gearpump-anomaly-detector",
    libraryDependencies ++= Seq(
      "org.apache.gearpump" %% "gearpump-core" % gearpumpVersion % "provided",
      "org.apache.gearpump" %% "gearpump-streaming" % gearpumpVersion % "provided"
        exclude("org.apache.gearpump", "gearpump-shaded-metrics-graphite"),
      "org.apache.gearpump" %% "gearpump-external-kafka" % gearpumpVersion % "provided",
      "org.apache.gearpump" %% "gearpump-external-monoid" % gearpumpVersion % "provided",
      "com.codahale.metrics" % "metrics-jvm" % codahaleVersion
    ),
    test in assembly := {},
    assemblyOption in assembly ~= { _.copy(includeScala = false) }
  )


