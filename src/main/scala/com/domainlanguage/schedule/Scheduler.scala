package com.domainlanguage.schedule

import scala.swing._
import scala.collection.mutable._
import java.io.File
import scala.Predef._
import javax.swing.event.{TableModelEvent, TableModelListener}
import javax.swing.filechooser.FileFilter
import scala.swing.event.ButtonClicked
import java.awt.Dimension

/**
 * User: Vladimir Gitlevich
 * Date: 10/8/13
 * Time: 23:27
 */
object Scheduler extends SimpleSwingApplication {

  import Conversions._

  def top = {
    new MainFrame with TableModelListener with ScheduleExporter {
      val repository = new FileBasedScheduleRepository()
      var scheduleFile: File = null

      title = "Domain Language Class Scheduler"

      var isScheduleChanged = false
      var schedulePane = new SchedulePane(schedule2Grid(new Schedule(), this))

      val fileChooser = new FileChooser()
      fileChooser.fileSelectionMode = FileChooser.SelectionMode.FilesOnly
      fileChooser.fileFilter = new FileFilter() {
        override def accept(f: File): Boolean = f.getName.endsWith(".json")
        override def getDescription = "json files (.json)"
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

      val saveMenuItem = new MenuItem(Action("Save") {
        saveSchedule()
      })
      saveMenuItem.enabled = false

      val exportHtmlMenuItem = new MenuItem(Action("Export HTML") {
        val dirChooser = new FileChooser()
        dirChooser.fileSelectionMode = FileChooser.SelectionMode.DirectoriesOnly
        if (dirChooser.showOpenDialog(schedulePane) == FileChooser.Result.Approve) {
          val exportDir = dirChooser.selectedFile
          exportHtml(grid2Schedule(schedulePane.table.model.asInstanceOf[Grid]), exportDir)
        }
      })
      exportHtmlMenuItem.enabled = false

      val openMenuItem = new MenuItem(Action("Open") {
        if (fileChooser.showOpenDialog(schedulePane) == FileChooser.Result.Approve) {
          scheduleFile = fileChooser.selectedFile
          val schedule = repository.findBy(FileScheduleSpec(scheduleFile))
          schedulePane = new SchedulePane(schedule2Grid(schedule, this))
          saveMenuItem.enabled = true
          contentPanel.contents.update(0, schedulePane)
          contentPanel.revalidate()
        }
      })

      menuBar = new MenuBar {
        contents += new Menu("File") {
          contents += openMenuItem
          contents += saveMenuItem
          contents += new Separator
          contents += exportHtmlMenuItem
          contents += new Separator
          contents += new MenuItem(Action("Quit") {
            showCloseDialog()
          })
        }
      }

      listenTo(addEntryButton, removeEntryButton, this)
      reactions += {
        case ButtonClicked(`addEntryButton`) => schedulePane.addEmptyRow()
        case ButtonClicked(`removeEntryButton`) => schedulePane.removeSelectedRow()
      }

      contents = contentPanel

      def tableChanged(event: TableModelEvent): Unit = {
        isScheduleChanged = true
        saveMenuItem.enabled = true
        exportHtmlMenuItem.enabled = true
      }

      def saveSchedule() {
        if (isScheduleChanged) {
          if (scheduleFile == null && fileChooser.showSaveDialog(schedulePane) == FileChooser.Result.Approve) {
              scheduleFile = fileChooser.selectedFile
          }

          if (scheduleFile != null) {
            repository.save(scheduleFile, grid2Schedule(schedulePane.table.model.asInstanceOf[Grid]))
            isScheduleChanged = false
          }
        }
      }

      private def showCloseDialog() {
        if (!isScheduleChanged) quit()

        Dialog.showConfirmation(
          parent = null,
          title = "Exit",
          message = "Save schedule before exit?",
          optionType = Dialog.Options.YesNoCancel
        ) match {
          case Dialog.Result.Yes =>
            saveSchedule()
            quit()
          case Dialog.Result.No =>
            quit()
          case _ => ()
        }
      }
    }
  }

}


object Conversions {
  def grid2Schedule(grid: Grid): Schedule = {

    val entries = for (r <- grid.rows)
    yield Event(r(0).value, r(1).value, r(2).value, r(3).value, r(4).value, r(5).value, r(6).value, r(7).value)

    Schedule(entries.toList)
  }

  def schedule2Grid(schedule: Schedule, listener: TableModelListener): Grid = {
    val cells = ListBuffer[ListBuffer[GridCell]]()

    schedule.events.foreach {
      entry =>
        val row = ListBuffer[GridCell]()
        row += GridCell(entry.country)
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