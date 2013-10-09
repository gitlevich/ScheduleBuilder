package com.domainlanguage.schedule

import java.util.Date

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
}

case class ScheduleEntry(country: String, city: String, date: Date, instructor: String, entryName: String, pricing: String, bookingPrompt: String, bookingUrl: String)