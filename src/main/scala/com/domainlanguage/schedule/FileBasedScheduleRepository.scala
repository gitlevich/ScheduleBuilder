package com.domainlanguage.schedule

import scala.util.parsing.json.{JSONObject, JSON}
import java.io.File

/**
 * User: Vladimir Gitlevich
 * Date: 10/9/13
 * Time: 15:58
 */
class FileBasedScheduleRepository extends ScheduleRepository {

  def findBy(spec: ScheduleSpec): Schedule = {
    spec match {
      case s: FileScheduleSpec =>
        toSchedule(readFromFile(s.file))
      case s: ClassPathScheduleSpec =>
        toSchedule(readFromClasspath(s.fileName))
    }
  }

  def save(name: String, schedule: Schedule): Unit = {
    println(s"Saving schedule with name $name")
  }

  def save(file: File, schedule: Schedule): Unit = {
    println(s"Saving schedule with name ${file.getAbsoluteFile}")
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

  def toEntry(p: Map[String, String]): ScheduleEntry = {
    ScheduleEntry(p("country"), p("city"), p("date"), p("instructor"), p("className"), p("pricing"), p("bookingPrompt"), p("bookingUrl"))
  }

  def toSchedule(jsonString: String): Schedule = {
    val schedulePrototype = JSON.parseFull(jsonString).getOrElse(new JSONObject(Map.empty[String, Any])).asInstanceOf[Map[String, Any]]
    val entryPrototypes = schedulePrototype("schedule").asInstanceOf[List[Map[String, String]]]

    Schedule(entryPrototypes.map(p => toEntry(p)).toList)
  }
}