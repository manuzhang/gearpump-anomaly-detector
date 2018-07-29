package io.github.manuzhang.window

import org.apache.gearpump.Message
import org.apache.gearpump.cluster.UserConfig
import org.apache.gearpump.streaming.task.{Task, TaskContext}

import scala.concurrent.duration._

case class WindowCounts(word: String, count: Long)

/**
 * Counts word frequencies within eventTime based window
 */
class WindowCounter(taskContext: TaskContext, conf: UserConfig)
    extends Task(taskContext, conf) {

  private val windowSize = 1.seconds
  private var windowCounts = Map.empty[String, Long]
  private var lastUpstreamMinClock: Option[Long] = None


  override def onNext(message: Message): Unit = {
    message.value match {
      case word: String =>
        if (!windowCounts.contains(word)) {
          windowCounts += word -> 1L
        }
        val count = windowCounts(word)

        windowCounts += word -> (count + 1)

        val curUpstreamMinClock = taskContext.upstreamMinClock
        if (lastUpstreamMinClock.isDefined && (curUpstreamMinClock - lastUpstreamMinClock.get) > windowSize.toMillis) {
          taskContext.output(Message(WindowCounts(word, count), curUpstreamMinClock))
          windowCounts -= word
          lastUpstreamMinClock = Some(curUpstreamMinClock)
        } else if (lastUpstreamMinClock.isEmpty) {
          lastUpstreamMinClock = Some(curUpstreamMinClock)
        }
    }

  }

}
