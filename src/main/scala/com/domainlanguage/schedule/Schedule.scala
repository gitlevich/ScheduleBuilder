package com.domainlanguage.schedule

import java.io.File
import scala.util.parsing.json.{JSONObject, JSON}


/**
 * User: Vladimir Gitlevich
 * Date: 10/7/13
 * Time: 19:46
 */

case class Schedule(events: List[Event] = List()) {
  val eventCount = events.length

  def withNewEntry(event: Event): Schedule = {
    Schedule(events :+ event)
  }

  def eventsByCountry(country: String): List[Event] = {
    events.filter(event => event.country == country)
  }

  def listEventCountries(): Set[String] = {
    val all = for(event <- events) yield event.country
    all.toSet[String]
  }

  def toJsonString: String = {
    val entriesJson = for(entry <- events) yield entry.toJsonString
    s"""{
    "schedule": [ ${entriesJson.mkString (",\n\t")}
    ]
}
"""
  }
}

case class Event(country: String, city: String, date: String, instructor: String, eventName: String,
                         pricing: String, bookingPrompt: String, bookingUrl: String) {

  def toJsonString: String =
    s""" {
            "${Schedule.country}": "$country",
            "${Schedule.city}": "$city",
            "${Schedule.date}": "$date",
            "${Schedule.instructor}": "$instructor",
            "${Schedule.eventName}": "$eventName",
            "${Schedule.pricing}": "$pricing",
            "${Schedule.bookingPrompt}": "$bookingPrompt",
            "${Schedule.bookingUrl}": "$bookingUrl"
        } """
}

object Schedule {
  val country = "country"
  val city = "city"
  val date = "date"
  val instructor = "instructor"
  val eventName = "eventName"
  val pricing = "pricing"
  val bookingPrompt = "bookingPrompt"
  val bookingUrl = "bookingUrl"

  def fromJsonString(jsonString: String): Schedule = {
    val schedulePrototype = JSON.parseFull(jsonString).getOrElse(new JSONObject(Map.empty[String, Any])).asInstanceOf[Map[String, Any]]
    val entryPrototypes = schedulePrototype("schedule").asInstanceOf[List[Map[String, String]]]

    Schedule(entryPrototypes.map(p => toEvent(p)).toList)
  }

  private def toEvent(m: Map[String, String]): Event = {
    Event(m(country), m(city), m(date), m(instructor), m(eventName), m(pricing), m(bookingPrompt), m(bookingUrl))
  }
}

object Event {
  val columnNames: Array[String] = Array("Country", "City", "Date", "Instructor", "Entry Name", "Pricing",
    "Booking Prompt", "Booking link")
}

trait ScheduleRepository {
  def findBy(spec: ScheduleSpec): Schedule
  def save(file: File, schedule: Schedule): Unit
}

trait ScheduleSpec
case class FileScheduleSpec(file: File) extends ScheduleSpec
case class ClassPathScheduleSpec(fileName: String) extends ScheduleSpec