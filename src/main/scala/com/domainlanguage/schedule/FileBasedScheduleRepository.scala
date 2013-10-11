package com.domainlanguage.schedule

import java.io.{BufferedWriter, FileWriter, PrintWriter, File}
import grizzled.slf4j.Logging

/**
 * User: Vladimir Gitlevich
 * Date: 10/9/13
 * Time: 15:58
 */
class FileBasedScheduleRepository extends ScheduleRepository with Logging {

  def findBy(spec: ScheduleSpec): Schedule = {
    spec match {
      case s: FileScheduleSpec =>
        Schedule.fromJsonString(readFromFile(s.file))
      case s: ClassPathScheduleSpec =>
        Schedule.fromJsonString(readFromClasspath(s.fileName))
    }
  }

  def save(name: String, schedule: Schedule): Unit = {
    debug(s"Saving schedule with name $name")
  }

  def save(file: File, schedule: Schedule): Unit = {
    val writer = new PrintWriter( new BufferedWriter(new FileWriter(file,false)))
    writer.println(schedule.toJsonString)
    writer.close()
  }

  def readFromClasspath(fileName: String): String = {
    require(fileName != null)

    val source = scala.io.Source.fromInputStream(classOf[FileBasedScheduleRepository].getClassLoader.getResourceAsStream(fileName))
    val lines = source.mkString
    source.close()

    lines
  }

  def readFromFile(file: File): String = {
    require(file != null)

    val source = scala.io.Source.fromFile(file)
    val lines = source.mkString
    source.close()

    lines
  }
}