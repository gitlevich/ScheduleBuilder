package com.domainlanguage.schedule

import scala.swing._
import scala.collection.mutable._
import scala.swing.event.ButtonClicked

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
    val schedulePane = new SchedulePane(schedule2Grid(schedule))
    val saveButton = new Button("Save")

    val flowPanel = new FlowPanel(schedulePane, saveButton)

    contents = flowPanel

    listenTo(saveButton)
    reactions += {
      case ButtonClicked(b) =>
        println(schedulePane.table.model)
        repository.saveWithName("schedule.json", grid2Schedule(schedulePane.table.model.asInstanceOf[Grid]))
    }
  }

  def grid2Schedule(grid: Grid): Schedule = {
    null
  }

  def schedule2Grid(schedule: Schedule): Grid = {
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

    Grid(cells, ScheduleEntry.columnNames)
  }
}
