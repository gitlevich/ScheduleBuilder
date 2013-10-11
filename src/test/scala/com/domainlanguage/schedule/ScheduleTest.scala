package com.domainlanguage.schedule

import org.scalatest.junit.ShouldMatchersForJUnit
import org.junit.Test

/**
 * User: Vladimir Gitlevich
 * Date: 10/10/13
 * Time: 17:29
 */
class ScheduleTest extends ShouldMatchersForJUnit {
  @Test def shouldConvertFromJsonString() {
    val schedule = Schedule.fromJsonString(json)
    val jsonString = schedule.toJsonString

    jsonString.replaceAll("[\\t\\s]", "") should equal (json.replaceAll("[\\t\\s]", ""))
  }

  val json =
    """
      |{
      |    "schedule": [
      |        {
      |            "country": "NORTH AMERICA",
      |            "city": "San Francisco",
      |            "date": "Nov 15, 2013",
      |            "instructor": "Eric Evans",
      |            "className": "DDD Overview",
      |            "pricing": "See QCon SF site.",
      |            "bookingPrompt": "Book Online »",
      |            "bookingUrl": "http://domainlanguage.com/training/schedule/DDDoNYC2013-11-15C"
      |        },
      |        {
      |            "country": "NORTH AMERICA",
      |            "city": "New York",
      |            "date": "Jan 21, 2014",
      |            "instructor": "Eric Evans",
      |            "className": "DDD Immersion",
      |            "pricing": "$3,090 $2,790 if booked by 8/5",
      |            "bookingPrompt": "Book Online »",
      |            "bookingUrl": "http://domainlanguage.com/training/schedule/DDDmNYC2014-01-21P"
      |        }
      |    ]
      |}
      |
    """.stripMargin

}
