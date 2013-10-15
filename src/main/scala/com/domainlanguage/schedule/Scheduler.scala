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
import java.lang.Exception

/**
 * User: Vladimir Gitlevich
 * Date: 10/8/13
 * Time: 23:27
 */
object Scheduler extends SimpleSwingApplication with Logging {

  val repository = new FileBasedScheduleRepository()
  val config = SchedulerConfiguration(new File("workspace"), "schedule.json")

  SetUp(config).setUp() match {
    case Some(error) => {
      showErrorAndQuit(error)
    }
    case None =>
  }

  def top = {
    new MainFrame with TableModelListener with ScheduleExporter with FTPUpload {

      title = "Domain Language Class Scheduler"

      var schedulePane: SchedulePane = null
        loadSchedule() match {
          case Left(schedule) =>
            schedulePane = new SchedulePane(schedule2Grid(schedule, this))
          case Right(e) =>
            showErrorAndQuit(s"Unable to open schedule file ${config.scheduleFile.getAbsoluteFile}\n${e.getMessage}")
        }

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
          exportHtml(grid2Schedule(schedulePane.table.model.asInstanceOf[Grid]), config.exportDir)
      })

      val exportAndPublishMenuItem = new MenuItem(Action("Export and Publish") {
        exportHtml(grid2Schedule(schedulePane.table.model.asInstanceOf[Grid]), config.exportDir)
        upload(config.exportDir.listFiles().toList, FtpDestinationSpec(loadProperties(config.properties)))
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



  private def showErrorAndQuit(error: String) {
    Dialog.showMessage(message = error, messageType = Dialog.Message.Error)
    quit()
  }

  private def loadSchedule(): Either[Schedule, Exception] = {
    try {
      Left(repository.findBy(FileScheduleSpec(config.scheduleFile)))
    }
    catch {
      case e: Exception =>
        error(s"Unable to open schedule file ${config.scheduleFile}", e)
        Right(e)
    }
  }

}

case class SetUp(config: SchedulerConfiguration) extends FilePersistence with Logging {
  def setUp(): Option[String] = {
    debug(s"""Work dir: "${config.workingDir.getCanonicalPath}", schedule: "${config.properties}", props: "${config.properties}""")

    val errors = ListBuffer[String]()
    if(!config.properties.exists()) {
      errors += s"""Properties file "${config.properties}" is missing.\nPlease have someone add it."""
    }

    if(!config.exportDir.exists()) config.exportDir.mkdirs()

    if(!config.scheduleFile.exists()) {
      try {
        writeToFile(config.scheduleFile, readFromClasspath("schedule.json"))
        debug(s"Created sample schedule file ${config.scheduleFile}")
      }
      catch {
        case e: Exception =>
          errors += e.getMessage
          error(s"Unable to create ${config.scheduleFile}", e)
      }
    }

    if(errors.isEmpty) None else Some(errors.mkString("\n"))
  }
}

case class SchedulerConfiguration(workingDir: File, scheduleFileName: String) {
  val scheduleFile = new File(workingDir, scheduleFileName)
  val properties = new File(workingDir, "scheduler.properties")
  val exportDir = new File(workingDir, "export")
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