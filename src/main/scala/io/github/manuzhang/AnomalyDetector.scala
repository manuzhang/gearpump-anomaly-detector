package io.github.manuzhang


import _root_.io.github.manuzhang.detector.Detector
import _root_.io.github.manuzhang.io.{SinkSimulator, SourceSimulator}
import _root_.io.github.manuzhang.model.ModelCalculator
import _root_.io.github.manuzhang.window.WindowCounter
import akka.actor.ActorSystem
import org.apache.gearpump.cluster.UserConfig
import org.apache.gearpump.cluster.client.ClientContext
import org.apache.gearpump.cluster.main.{ParseResult, CLIOption, ArgumentsParser}
import org.apache.gearpump.streaming.{Processor, StreamApplication}
import org.apache.gearpump.streaming.sink.DataSinkProcessor
import org.apache.gearpump.streaming.source.DataSourceProcessor
import org.apache.gearpump.util.{Graph, LogUtil, AkkaApp}
import org.apache.gearpump.util.Graph._
import org.slf4j.Logger

object AnomalyDetector extends AkkaApp with ArgumentsParser {
  private val LOG: Logger = LogUtil.getLogger(getClass)

  override val options: Array[(String, CLIOption[Any])] = Array(
  )

  def application(config: ParseResult, system: ActorSystem): StreamApplication = {
    implicit val actorSystem = system
    val appName = "AnomalyDetector"

    val appConfig = UserConfig.empty
    val source = new SourceSimulator
    val sourceProcessor = DataSourceProcessor(source)
    val windowCounter = Processor[WindowCounter](1)
    val modelCalculator = Processor[ModelCalculator](1)
    val detector = Processor[Detector](1)
    val sink = new SinkSimulator
    val sinkProcessor = DataSinkProcessor(sink)
    val graph = Graph(
      sourceProcessor ~> windowCounter,
      windowCounter ~> modelCalculator,
      windowCounter ~> detector,
      modelCalculator ~> detector,
      detector ~> sinkProcessor)
    val app = StreamApplication(appName, graph, appConfig)
    app
  }

  override def main(akkaConf: Config, args: Array[String]): Unit = {
    val config = parse(args)
    val context = ClientContext(akkaConf)
    val appId = context.submit(application(config, context.system))
    context.close()
  }
}
