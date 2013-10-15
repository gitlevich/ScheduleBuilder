package com.domainlanguage.schedule

import org.scalatest.junit.ShouldMatchersForJUnit
import org.junit.Test
import scala.collection.mutable.ListBuffer

/**
 * User: Vladimir Gitlevich
 * Date: 10/10/13
 * Time: 11:05
 */
class ConversionsTest extends ShouldMatchersForJUnit {

  import Schedule._
  val cells = ListBuffer(
    ListBuffer(
      GridCell(region),
      GridCell(state),
      GridCell(city),
      GridCell(date),
      GridCell(instructor),
      GridCell(eventName),
      GridCell(pricing),
      GridCell(bookingPrompt),
      GridCell(bookingUrl)))

  @Test def shouldConvertGridToSchedule(): Unit = {
    val grid = Grid(cells, Event.columnNames)

    val schedule = Conversions.grid2Schedule(grid)

    schedule should not be null
    schedule.eventCount should be (1)

    val entry = schedule.events(0)
    entry.region should be (region)
    entry.state should be (state)
    entry.city should be (city)
    entry.date should be (date)
    entry.instructor should be (instructor)
    entry.eventName should be (eventName)
    entry.pricing should be (pricing)
    entry.bookingPrompt should be (bookingPrompt)
    entry.bookingUrl should be (bookingUrl)
  }

}
