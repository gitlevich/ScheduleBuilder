package com.domainlanguage.schedule

import org.scalatest.junit.ShouldMatchersForJUnit
import org.junit.Test

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
    firstEntry.country should be ("NORTH AMERICA")
    firstEntry.city should be ("San Francisco")
    firstEntry.date should be ("Nov 15, 2013")
    firstEntry.instructor should be ("Eric Evans")
    firstEntry.eventName should be ("DDD Overview")
    firstEntry.pricing should be ("See QCon SF site.")
    firstEntry.bookingPrompt should startWith ("Book Online&nbsp;&raquo;")
    firstEntry.bookingUrl should be ("http://domainlanguage.com/training/schedule/DDDoNYC2013-11-15C")
  }
}
