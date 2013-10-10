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

  val cells = ListBuffer(ListBuffer(GridCell("a"), GridCell("b"), GridCell("c"), GridCell("d"), GridCell("e"),
    GridCell("f"), GridCell("g"), GridCell("h")))

  @Test def shouldConvertGridToSchedule(): Unit = {
    val grid = Grid(cells, ScheduleEntry.columnNames)

    val schedule = Conversions.grid2Schedule(grid)

    schedule should not be null
    schedule.entryCount should be (1)

    val entry = schedule.entries(0)
    entry.country should be ("a")
    entry.city should be ("b")
    entry.date should be ("c")
    entry.instructor should be ("d")
    entry.entryName should be ("e")
    entry.pricing should be ("f")
    entry.bookingPrompt should be ("g")
    entry.bookingUrl should be ("h")
  }

}
