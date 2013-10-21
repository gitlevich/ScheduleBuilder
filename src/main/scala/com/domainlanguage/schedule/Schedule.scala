package com.domainlanguage.schedule

import java.io.File
import scala.util.parsing.json.{JSONObject, JSON}
import java.util.Date


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

  def sorted: Schedule = {
    Schedule(events.sortBy(event => event.date))
  }

  def eventsByCountry(country: String): List[Event] =
    events.filter(event => event.region == country)

  val eventCountries = {
    val all = for(event <- events) yield event.region
    all.toSet[String].toList
  }

  val eventNames = {
    val all = for(event <- events) yield event.eventName
    all.toSet[String].toList
  }

  def eventsByName(name: String): List[Event] = {
    events.filter( event => event.eventName == name)
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

case class Event(region: String, state: String, city: String, date: Option[Date], instructor: String, eventName: String,
                         pricing: String, bookingPrompt: String, bookingUrl: String) {

  def toJsonString: String = {
    val dateString: String = date match {
      case None => ""
      case Some(d) => d
    }
    s""" {
            "${Schedule.region}": "$region",
            "${Schedule.state}": "$state",
            "${Schedule.city}": "$city",
            "${Schedule.date}": "$dateString",
            "${Schedule.instructor}": "$instructor",
            "${Schedule.eventName}": "$eventName",
            "${Schedule.pricing}": "$pricing",
            "${Schedule.bookingPrompt}": "$bookingPrompt",
            "${Schedule.bookingUrl}": "$bookingUrl"
        } """
  }
}

object Schedule {
  val region = "region"
  val state = "state"
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

    Schedule(entryPrototypes.map(p => toEvent(p)).toList).sorted
  }

  private def toEvent(m: Map[String, String]): Event = {
    Event(m(region), m(state), m(city), Option(m(date)), m(instructor), m(eventName), m(pricing), m(bookingPrompt), m(bookingUrl))
  }
}

object Event {
  val columnNames: Array[String] = Array("Region", "State/Country", "City", "Date", "Instructor", "Entry Name", "Pricing",
    "Booking Prompt", "Booking link")
}

trait ScheduleRepository {
  def findBy(spec: ScheduleSpec): Schedule
  def save(file: File, schedule: Schedule): Unit
}

sealed trait ScheduleSpec
case class FileScheduleSpec(file: File) extends ScheduleSpec
case class ClassPathScheduleSpec(fileName: String) extends ScheduleSpec