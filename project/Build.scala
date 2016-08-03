import sbt.Keys._
import sbt._
import sbtassembly.Plugin.AssemblyKeys._
import sbtassembly.Plugin._

import scala.collection.immutable.Map.WithDefault

object Build extends sbt.Build {

  val akkaVersion = "2.4.3"
  val gearpumpVersion = "0.8.1-SNAPSHOT"
  val junitVersion = "4.12"
  val kafkaVersion = "0.8.2.1"
  val mockitoVersion = "1.10.17"
  val codahaleVersion = "3.0.2"

  val scalaVersionMajor = "scala-2.11"
  val scalaVersionNumber = "2.11.8"

  val commonSettings = Defaults.defaultSettings ++ 
    Seq(
      resolvers ++= Seq(
        Resolver.mavenLocal,
        "apache-repo" at "https://repository.apache.org/content/repositories",
        "maven-repo" at "http://repo.maven.apache.org/maven2",
        "maven1-repo" at "http://repo1.maven.org/maven2",
        "maven2-repo" at "http://mvnrepository.com/artifact"
      )
    ) ++
    Seq(
      scalaVersion := scalaVersionNumber,
      organization := "io.github.manuzhang",
      scalacOptions ++= Seq("-Yclosure-elim","-Yinline"),
      publishMavenStyle := true,
      pomIncludeRepository := { _ => false }
  )

  val myAssemblySettings = assemblySettings ++ Seq(
    test in assembly := {},
    assemblyOption in assembly ~= { _.copy(includeScala = true) }
  )

  lazy val root = Project(
    id = "gearpump-anomaly-detector",
    base = file("."),
    settings = commonSettings ++ myAssemblySettings ++ Seq(
      libraryDependencies ++= Seq(
        "org.apache.gearpump" %% "gearpump-streaming" % gearpumpVersion % "provided"
          exclude("org.apache.gearpump", "gearpump-shaded-metrics-graphite"),
        "org.apache.gearpump" %% "gearpump-external-kafka" % gearpumpVersion % "provided",
        "org.apache.gearpump" %% "gearpump-external-serializer" % gearpumpVersion % "provided",
        "org.apache.gearpump" %% "gearpump-external-monoid" % gearpumpVersion % "provided",
        "com.codahale.metrics" % "metrics-jvm" % codahaleVersion
      )
    )
  )

}
