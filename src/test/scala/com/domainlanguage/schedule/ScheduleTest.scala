package com.domainlanguage.schedule

import org.scalatest.junit.ShouldMatchersForJUnit
import org.junit.Test

/**
 * User: Vladimir Gitlevich
 * Date: 10/10/13
 * Time: 17:29
 */
class ScheduleTest extends ShouldMatchersForJUnit with FilePersistence {
  @Test def shouldConvertFromJsonString() {
    val schedule = Schedule.fromJsonString(json)
    val jsonString = schedule.toJsonString

    normalized(jsonString) should equal (normalized(json))
  }

  @Test def shouldListEventsByCountry() {
    val schedule = Schedule.fromJsonString(json)
    
    val usEvents = schedule.eventsByCountry("NORTH AMERICA")
    
    usEvents.length should be (2)
    usEvents(0).city should be ("San Francisco")
    usEvents(1).city should be ("New York")

    val ukEvents = schedule.eventsByCountry("UK")
    ukEvents.length should be (1)
    ukEvents(0).city should be ("London")
  }

  @Test def shouldListCountriesWhereEventsHeld() {
    val schedule = Schedule.fromJsonString(json)

    val countries = schedule.eventCountries

    countries.size should be (2)
    countries should contain ("NORTH AMERICA")
    countries should contain ("UK")
  }

  @Test def shouldListEventNames() {
    val schedule = Schedule.fromJsonString(json)

    val names = schedule.eventNames

    names.size should be (2)
    names should contain ("DDD Immersion")
    names should contain ("DDD Overview")
  }

  @Test def shouldListEventsByName() {
    val schedule = Schedule.fromJsonString(json)

    schedule.eventsByName("DDD Immersion").length should be (2)
    schedule.eventsByName("DDD Overview").length should be (1)
  }

  private def normalized(string: String): String = {
    string.replaceAll("[\\t\\s]", "")
  }

  val json =
    """
      |{
      |    "schedule": [
      |        {
      |            "region": "NORTH AMERICA",
      |            "state": "CA",
      |            "city": "San Francisco",
      |            "date": "Nov 15, 2013",
      |            "instructor": "Eric Evans",
      |            "eventName": "DDD Overview",
      |            "pricing": "See QCon SF site.",
      |            "bookingPrompt": "Book Online",
      |            "bookingUrl": "http://domainlanguage.com/training/schedule/DDDoNYC2013-11-15C"
      |        },
      |        {
      |            "region": "NORTH AMERICA",
      |            "state": "NY",
      |            "city": "New York",
      |            "date": "Jan 21, 2014",
      |            "instructor": "Eric Evans",
      |            "eventName": "DDD Immersion",
      |            "pricing": "$3,090 $2,790 if booked by 8/5",
      |            "bookingPrompt": "Book Online",
      |            "bookingUrl": "http://domainlanguage.com/training/schedule/DDDmNYC2014-01-21P"
      |        },
      |        {
      |            "region": "UK",
      |            "state": "UK",
      |            "city": "London",
      |            "date": "Jan 21, 2014",
      |            "instructor": "Someone Else",
      |            "eventName": "DDD Immersion",
      |            "pricing": "$3,090 $2,790 if booked by 8/5",
      |            "bookingPrompt": "Book Online",
      |            "bookingUrl": "http://domainlanguage.com/training/schedule/DDDmNYC2014-01-21P"
      |        }
      |    ]
      |}
      |
    """.stripMargin

}
