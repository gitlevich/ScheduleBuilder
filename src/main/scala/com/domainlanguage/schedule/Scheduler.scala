package com.domainlanguage.schedule

import scala.swing._
import scala.collection.mutable._
import java.io.File

/**
 * User: Vladimir Gitlevich
 * Date: 10/8/13
 * Time: 23:27
 */
object Scheduler extends SimpleSwingApplication {
  import Conversions._

  val repository = new FileBasedScheduleRepository()

  def top = {
    var file: File = null
    var schedule: Schedule = Schedule()
    var schedulePane: SchedulePane = null

    new MainFrame {
      title = "Domain Language Class Scheduler"

      schedulePane = new SchedulePane(schedule2Grid(schedule))
      val fileChooser = new FileChooser()

      val saveMenuItem = new MenuItem(Action("Save") {
        println(schedulePane.table.model)
        repository.save(file, grid2Schedule(schedulePane.table.model.asInstanceOf[Grid]))
      })
      saveMenuItem.enabled = false

      val openMenuItem = new MenuItem(Action("Open") {
        if (fileChooser.showOpenDialog(schedulePane) == FileChooser.Result.Approve) {
          file = fileChooser.selectedFile
          schedule = repository.findBy(FileScheduleSpec(file))
          schedulePane = new SchedulePane(schedule2Grid(schedule))
          saveMenuItem.enabled = true
          contents = schedulePane
        }
      })

      menuBar = new MenuBar {
        contents += new Menu("File") {
          contents += openMenuItem
          contents += saveMenuItem
          contents += new Separator
          contents += new MenuItem(Action("Quit") {
            println("quitting")
            if(file != null) {
              repository.save(file, grid2Schedule(schedulePane.table.model.asInstanceOf[Grid]))
            }
            System.exit(0)
          })
        }
      }

      contents = schedulePane
    }
  }

}


object Conversions {
  def grid2Schedule(grid: Grid): Schedule = {

    val entries = for(r <- grid.rows)
      yield ScheduleEntry(r(0).value, r(1).value, r(2).value, r(3).value, r(4).value, r(5).value, r(6).value, r(7).value)

    Schedule(entries.toList)
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