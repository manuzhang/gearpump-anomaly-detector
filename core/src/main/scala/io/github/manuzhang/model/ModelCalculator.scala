package io.github.manuzhang.model

import com.codahale.metrics.ExponentiallyDecayingReservoir
import io.github.manuzhang.window.WindowCounts
import org.apache.gearpump.Message
import org.apache.gearpump.cluster.UserConfig
import org.apache.gearpump.streaming.task.{Task, TaskContext}

case class Model(word: String, min: Long, max: Long)

class ModelCalculator (taskContext: TaskContext, conf: UserConfig)
    extends Task(taskContext, conf) {

  private var models = Map.empty[String, ExponentiallyDecayingReservoir]

  override def onNext(message: Message): Unit = {
    message.value match {
      case WindowCounts(word, count) =>
        if (!models.contains(word)) {
          models += word -> new ExponentiallyDecayingReservoir()
        }
        val reservoir = models(word)
        reservoir.update(count)
        val snapshot = reservoir.getSnapshot
        taskContext.output(Message(Model(word,
          snapshot.getMean.toLong, snapshot.get75thPercentile.toLong),
          message.timestamp))
    }
  }

}
