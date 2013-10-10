package com.domainlanguage.schedule

import scala.collection.mutable.ListBuffer
import java.io.File


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
}

case class ScheduleEntry(country: String, city: String, date: String, instructor: String, entryName: String,
                         pricing: String, bookingPrompt: String, bookingUrl: String)
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