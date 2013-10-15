package com.domainlanguage.schedule

import scala.swing._
import scala.collection.mutable._
import java.io.File
import scala.Predef._
import javax.swing.event.{TableModelEvent, TableModelListener}
import scala.swing.event.ButtonClicked
import java.awt.Dimension
import grizzled.slf4j.Logging
import Conversions._

/**
 * User: Vladimir Gitlevich
 * Date: 10/8/13
 * Time: 23:27
 */
object Scheduler extends SimpleSwingApplication with Logging {

  val config = SchedulerConfiguration(new File("./workspace"), "schedule.json")
  SetUp(config).setUp() match {
    case Some(error) => {
      Dialog.showMessage(message = error)
      quit()
    }
    case None =>
  }

  def top = {
    new MainFrame with TableModelListener with ScheduleExporter {
      val repository = new FileBasedScheduleRepository()

      title = "Domain Language Class Scheduler"

      var schedulePane = new SchedulePane(schedule2Grid(repository.findBy(FileScheduleSpec(config.scheduleFile)), this))

      val addEntryButton = new Button("Add event")
      addEntryButton.tooltip = "Add a new event to the schedule"

      val removeEntryButton = new Button("Remove event")
      removeEntryButton.tooltip = "Remove the currently selected event from the schedule"

      val buttonPanel = new BoxPanel(Orientation.Horizontal) {
        preferredSize = new Dimension(1150, 20)
        contents += addEntryButton
        contents += removeEntryButton
      }

      val contentPanel = new BoxPanel(Orientation.Vertical) {
        preferredSize = new Dimension(1200, 500)
        contents += schedulePane
        contents += buttonPanel
        border = Swing.EmptyBorder(30, 30, 10, 30)
      }

      val exportMenuItem = new MenuItem(Action("Export") {
          exportHtml(grid2Schedule(schedulePane.table.model.asInstanceOf[Grid]), config.workingDir)
      })

      val exportAndPublishMenuItem = new MenuItem(Action("Export and Publish") {
        exportHtml(grid2Schedule(schedulePane.table.model.asInstanceOf[Grid]), config.workingDir)
        // TODO add publishing bit
      })

      menuBar = new MenuBar {
        contents += new Menu("File") {
          contents += exportMenuItem
          contents += exportAndPublishMenuItem
          contents += new Separator
          contents += new MenuItem(Action("Quit") {
            quit()
          })
        }
      }

      listenTo(addEntryButton, removeEntryButton, this)
      reactions += {
        case ButtonClicked(`addEntryButton`) => schedulePane.addEmptyRow()
        case ButtonClicked(`removeEntryButton`) => schedulePane.removeSelectedRow()
      }

      contents = contentPanel

      def tableChanged(event: TableModelEvent) {
        repository.save(config.scheduleFile, grid2Schedule(schedulePane.table.model.asInstanceOf[Grid]))
      }
    }
  }
}

case class SetUp(config: SchedulerConfiguration) extends FilePersistence {
  def setUp(): Option[String] = {
    debug(s"""
    |Launched with working dir ${config.workingDir.getCanonicalPath},
    | schedule file ${config.properties.getCanonicalPath}
    | and properties ${config.properties.getCanonicalPath}""".stripMargin)

    val errors = ListBuffer[String]()
    if(!config.properties.exists()) {
      errors += s"""Properties file "${config.properties.getAbsoluteFile}" is missing"""
      debug(s"properties file ${config.properties} not found")
    }

    if(!config.scheduleFile.exists()) {
      config.scheduleFile.getParentFile.mkdirs()
      writeToFile(config.scheduleFile, readFromClasspath("schedule.json"))
      debug(s"Created sample schedule file ${config.scheduleFile}")
    }

    debug(s"errors = $errors")
    if(errors.isEmpty) None else Some(errors.mkString("\n"))
  }

}

case class SchedulerConfiguration(workingDir: File, scheduleFileName: String) {
  val scheduleFile = new File(workingDir, s"$scheduleFileName")
  val properties = new File(workingDir, "scheduler.properties")
}


object Conversions {
  def grid2Schedule(grid: Grid): Schedule = {

    val entries = for (r <- grid.rows)
      yield Event(r(0).value, r(1).value, r(2).value, r(3).value, r(4).value, r(5).value, r(6).value, r(7).value, r(8).value)

    Schedule(entries.toList)
  }

  def schedule2Grid(schedule: Schedule, listener: TableModelListener): Grid = {
    val cells = ListBuffer[ListBuffer[GridCell]]()

    schedule.events.foreach {
      entry =>
        val row = ListBuffer[GridCell]()
        row += GridCell(entry.region)
        row += GridCell(entry.state)
        row += GridCell(entry.city)
        row += GridCell(entry.date)
        row += GridCell(entry.instructor)
        row += GridCell(entry.eventName)
        row += GridCell(entry.pricing)
        row += GridCell(entry.bookingPrompt)
        row += GridCell(entry.bookingUrl)

        cells += row
    }

    val grid = Grid(cells, Event.columnNames)
    grid.addTableModelListener(listener)
    grid
  }

}