package io.github.manuzhang.detector

import io.github.manuzhang.model.Model
import io.github.manuzhang.window.WindowCounts
import org.apache.gearpump.Message
import org.apache.gearpump.cluster.UserConfig
import org.apache.gearpump.streaming.task.{Task, TaskContext}

case class Anomaly(word: String, count: Long)

class Detector(taskContext: TaskContext, conf: UserConfig)
    extends Task(taskContext, conf) {

  private var models = Map.empty[String, Model]

  override def onNext(message: Message): Unit = {

    message.msg match {
      case WindowCounts(word, count) =>
        models.get(word).foreach { case Model(_, min, max) =>
          if (count < min || count > max) {
            taskContext.output(Message(Anomaly(word, count), message.timestamp))
          }
        }
      case m: Model =>
        models += m.word -> m
      case e =>
        throw new Exception(s"invalid message $e")
    }

  }
}
