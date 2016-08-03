package io.github.manuzhang.io

import io.github.manuzhang.detector.Anomaly
import org.apache.gearpump.Message
import org.apache.gearpump.streaming.sink.DataSink
import org.apache.gearpump.streaming.task.TaskContext
import org.apache.gearpump.util.LogUtil

object SinkSimulator {
  private val LOG = LogUtil.getLogger(classOf[SinkSimulator])
}
/**
 * simulate kafka sink
 */
class SinkSimulator extends DataSink {
  import io.github.manuzhang.io.SinkSimulator._

  override def open(context: TaskContext): Unit = {}

  override def write(message: Message): Unit = {
    message.msg match {
      case Anomaly(word, count) =>
        LOG.info(s"Anomaly!!! word: $word; count: $count")
      case e =>
        throw new Exception(s"invalid message $e")
    }
  }

  override def close(): Unit = {}
}
