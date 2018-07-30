import mill.scalalib.{SbtModule, PublishModule, DepSyntax}
import mill.scalalib.publish.{PomSettings, License, Developer, SCM}
import mill.modules.Assembly.Rule


object core extends SbtModule with PublishModule {
  def scalaVersion = "2.11.8"
  def artifactName = "gearpump-anomaly-detector"
  def publishVersion = "0.1.0-SNAPSHOT"

  def pomSettings = PomSettings(
    description = artifactName(),
    organization = "io.github.manuzhang",
    url = "https://github.com/manuzhang/gearpump-anomaly-detector",
    licenses = Seq(License.MIT),
    scm = SCM(
      "git://github.com/manuzhang/gearpump-anomaly-detector.git",
      "scm:git://github.com/manuzhang/gearpump-anomaly-detector.git"
    ),
    developers = Seq(
      Developer("manuzhang", "Manu Zhang","https://github.com/manuzhang")
    )
  )

  val gearpumpVersion = "0.8.4"

  def compileIvyDeps = Agg(
    ivy"org.apache.gearpump::gearpump-core:$gearpumpVersion",
    ivy"org.apache.gearpump::gearpump-streaming:$gearpumpVersion",
    ivy"org.apache.gearpump::gearpump-external-kafka:$gearpumpVersion",
    ivy"org.apache.gearpump::gearpump-external-monoid:$gearpumpVersion",
    ivy"com.codahale.metrics:metrics-jvm:3.0.2"
  )

  def scalaLibraryIvyDeps = T { Agg.empty }
}