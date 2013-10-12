package com.domainlanguage.schedule

import java.io.{BufferedWriter, FileWriter, PrintWriter, File}
import grizzled.slf4j.Logging
import scala.io.Source

/**
 * User: Vladimir Gitlevich
 * Date: 10/9/13
 * Time: 15:58
 */
class FileBasedScheduleRepository extends ScheduleRepository with FilePersistence {

  def findBy(spec: ScheduleSpec): Schedule =
    spec match {
      case s: FileScheduleSpec => Schedule.fromJsonString(readFromFile(s.file))
      case s: ClassPathScheduleSpec => Schedule.fromJsonString(readFromClasspath(s.fileName))
    }

  def save(file: File, schedule: Schedule): Unit = {
    writeToFile(file, schedule.toJsonString)
  }

}