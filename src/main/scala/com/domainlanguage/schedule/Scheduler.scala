package com.domainlanguage.schedule

import scala.swing._
import scala.collection.mutable._
import java.io.File
import scala.Predef._
import scala.swing.event.ButtonClicked
import javax.swing.event.{TableModelEvent, TableModelListener}
import javax.swing.filechooser.FileFilter

/**
 * User: Vladimir Gitlevich
 * Date: 10/8/13
 * Time: 23:27
 */
object Scheduler extends SimpleSwingApplication {

  import Conversions._

  def top = {
    new MainFrame with TableModelListener {
      val repository = new FileBasedScheduleRepository()
      var scheduleFile: File = null

      title = "Domain Language Class Scheduler"

      var isScheduleChanged = false
      var shouldSaveSchedule = true
      var schedule = Schedule()
      var schedulePane = new SchedulePane(schedule2Grid(schedule, this))

      val fileChooser = new FileChooser()
      fileChooser.fileSelectionMode = FileChooser.SelectionMode.FilesOnly
      fileChooser.fileFilter = new FileFilter() {
        override def accept(f: File): Boolean = f.getName.endsWith(".json")
        override def getDescription = "json files (.json)"
      }

      val addEntryButton = new Button("Add entry")
      val removeEntryButton = new Button("Remove entry")

      val boxPanel = new BoxPanel(Orientation.Vertical) {
        contents += schedulePane
        contents += addEntryButton
        contents += removeEntryButton
        border = Swing.EmptyBorder(30, 30, 10, 30)
      }

      val saveMenuItem = new MenuItem(Action("Save") {
        println(schedulePane.table.model)
        saveSchedule()
      })
      saveMenuItem.enabled = false

      val openMenuItem = new MenuItem(Action("Open") {
        if (fileChooser.showOpenDialog(schedulePane) == FileChooser.Result.Approve) {
          scheduleFile = fileChooser.selectedFile
          schedule = repository.findBy(FileScheduleSpec(scheduleFile))
          schedulePane = new SchedulePane(schedule2Grid(schedule, this))
          saveMenuItem.enabled = true
          boxPanel.contents.update(0, schedulePane)
          boxPanel.revalidate()
        }
      })

      menuBar = new MenuBar {
        contents += new Menu("File") {
          contents += openMenuItem
          contents += saveMenuItem
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

      contents = boxPanel

      def tableChanged(event: TableModelEvent): Unit = {
        println(s"Notified of table change: $event")
        isScheduleChanged = true
        saveMenuItem.enabled = true
      }

      def saveSchedule() {
        if (isScheduleChanged) {
          if (scheduleFile == null) {
            if (fileChooser.showSaveDialog(schedulePane) == FileChooser.Result.Approve) {
              scheduleFile = fileChooser.selectedFile
            }
          }
          repository.save(scheduleFile, grid2Schedule(schedulePane.table.model.asInstanceOf[Grid]))
          isScheduleChanged = false
        }
      }

      private def showCloseDialog() {
        if (!isScheduleChanged) quit()

        Dialog.showConfirmation(parent = null,
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
    yield ScheduleEntry(r(0).value, r(1).value, r(2).value, r(3).value, r(4).value, r(5).value, r(6).value, r(7).value)

    Schedule(entries.toList)
  }

  def schedule2Grid(schedule: Schedule, listener: TableModelListener): Grid = {
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
    grid.addTableModelListener(listener)
    grid
  }

}