package com.domainlanguage.schedule

import scala.collection.mutable.ListBuffer
import java.io.File
import scala.util.parsing.json.{JSONObject, JSON}


/**
 * User: Vladimir Gitlevich
 * Date: 10/7/13
 * Time: 19:46
 */

case class Schedule(entries: List[ScheduleEntry] = List()) {
  val entryCount = entries.length

  def withNewEntry(entry: ScheduleEntry): Schedule = {
    Schedule(entries :+ entry)
  }

  def indexOf(entry: ScheduleEntry): Int = {
    entries.indexOf(entry)
  }

  def toJsonString: String = {
    val entriesJson = for(entry <- entries) yield entry.toJsonString
    s"""{
    "schedule": [ ${entriesJson.mkString (",\n\t")}
    ]
}
"""
  }
}

case class ScheduleEntry(country: String, city: String, date: String, instructor: String, entryName: String,
                         pricing: String, bookingPrompt: String, bookingUrl: String) {

  def toJsonString: String =
    s""" {
            "country": "$country",
            "city": "$city",
            "date": "$date",
            "instructor": "$instructor",
            "className": "$entryName",
            "pricing": "$pricing",
            "bookingPrompt": "$bookingPrompt",
            "bookingUrl": "$bookingUrl"
        } """
}

object Schedule {
  def fromJsonString(jsonString: String): Schedule = {
    val schedulePrototype = JSON.parseFull(jsonString).getOrElse(new JSONObject(Map.empty[String, Any])).asInstanceOf[Map[String, Any]]
    val entryPrototypes = schedulePrototype("schedule").asInstanceOf[List[Map[String, String]]]

    Schedule(entryPrototypes.map(p => toEntry(p)).toList)
  }

  private def toEntry(m: Map[String, String]): ScheduleEntry = {
    ScheduleEntry(m("country"), m("city"), m("date"), m("instructor"), m("className"), m("pricing"), m("bookingPrompt"), m("bookingUrl"))
  }
}

object ScheduleEntry {
  val columnNames: Array[String] = Array("Country", "City", "Date", "Instructor", "Entry Name", "Pricing",
    "Booking Prompt", "Booking link")
}

trait ScheduleRepository {
  def findBy(spec: ScheduleSpec): Schedule
  def save(name: String, schedule: Schedule): Unit
  def save(file: File, schedule: Schedule): Unit
}

trait ScheduleSpec
case class FileScheduleSpec(file: File) extends ScheduleSpec
case class ClassPathScheduleSpec(fileName: String) extends ScheduleSpec