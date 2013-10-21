package com.domainlanguage.schedule

import org.scalatest.junit.ShouldMatchersForJUnit
import org.junit.Test
import java.text.SimpleDateFormat

/**
 * This test is based on the sample schedule destinationDir /resources/schedule.json on the classpath
 * User: Vladimir Gitlevich
 * Date: 10/9/13
 * Time: 16:02
 */
class FileBasedScheduleRepositoryTest extends ShouldMatchersForJUnit {

  val repository = new FileBasedScheduleRepository()

  @Test def shouldLoadFileFromClassPath(): Unit = {
    val contents = repository.readFromClasspath("schedule.json")
    contents should not be null
  }

  @Test def shouldConvertJsonToSchedule(): Unit = {
    val schedule = repository.findBy(ClassPathScheduleSpec("schedule.json"))

    schedule should not be null
    schedule.eventCount should equal (5)

    val firstEntry = schedule.events(0)
    firstEntry.region should be ("UK")
    firstEntry.state should be ("UK")
    firstEntry.city should be ("London")
    firstEntry.date should be (Option(new SimpleDateFormat(DateFormat).parse("Oct 28, 2013")))
    firstEntry.instructor should be ("Alberto Brandolini")
    firstEntry.eventName should be ("DDD Foundations")
    firstEntry.pricing should be ("<strike>£1195</strike> £1075 60 days prior")
    firstEntry.bookingPrompt should startWith ("Book Online")
    firstEntry.bookingUrl should be ("http://domainlanguage.com/training/schedule/DDDfLON-P")
  }
}
