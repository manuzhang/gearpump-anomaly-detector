package io.github.manuzhang.io

import java.time.Instant
import java.util.Random

import org.apache.gearpump.Message
import org.apache.gearpump.streaming.source.DataSource
import org.apache.gearpump.streaming.task.TaskContext

/**
 * simulate kafka source
 */
class SourceSimulator extends DataSource {

  private val random = new Random
  private val words = Array("gearpump", "streaming", "big data", "real time", "apache",
    "latency", "throughput", "metrics", "dag", "api")

  override def open(context: TaskContext, startTime: Instant): Unit = {}

  override def close(): Unit = {}

  override def read(): Message = {
    Message(words(random.nextInt(words.length)), System.currentTimeMillis())
  }

  override def getWatermark: Instant = Instant.now()
}
