package com.domainlanguage.schedule

import scala.util.parsing.json.{JSONObject, JSON}

/**
 * User: Vladimir Gitlevich
 * Date: 10/9/13
 * Time: 15:58
 */
class FileBasedScheduleRepository extends ScheduleRepository {

  def findByName(name: String): Schedule = {
    toSchedule(readFromClasspath(name))
  }

  def readFromClasspath(fileName: String): String = {
    require(fileName != null)

    val source = scala.io.Source.fromInputStream(classOf[FileBasedScheduleRepository].getClassLoader.getResourceAsStream(fileName))
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