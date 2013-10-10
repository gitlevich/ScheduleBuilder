package com.domainlanguage.schedule

import scala.swing._
import scala.collection.mutable._

/**
 * User: Vladimir Gitlevich
 * Date: 10/8/13
 * Time: 23:27
 */
object Scheduler extends SimpleSwingApplication {
  val repository = new FileBasedScheduleRepository()

  def top = new MainFrame {
    title = "Domain Language Class Scheduler"

    val schedule = repository.findByName("schedule.json")

    val cells = ListBuffer[ListBuffer[GridCell]]()

    schedule.entries.foreach {
      entry =>
        val row = ListBuffer[GridCell]()
        row += GridCell(entry.country)
        row += GridCell(entry.city)
        row += GridCell(entry.date)
        row += GridCell(entry.instructor)
        row += GridCell(entry.entryName)
        row += GridCell(entry.pricing)
        row += GridCell(entry.bookingPrompt)
        row += GridCell(entry.bookingUrl)

        cells += row
    }

    val grid = Grid(cells, ScheduleEntry.columnNames)

    contents = new SchedulePane(grid)
  }
}
