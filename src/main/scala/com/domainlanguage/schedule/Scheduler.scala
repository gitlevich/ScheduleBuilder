package com.domainlanguage.schedule

import scala.swing._
import scala.collection.mutable._
import java.io.File
import scala.Predef._
import scala.swing.event.ButtonClicked
import javax.swing.event.{TableModelEvent, TableModelListener}

/**
 * User: Vladimir Gitlevich
 * Date: 10/8/13
 * Time: 23:27
 */
object Scheduler extends SimpleSwingApplication with TableModelListener {

  import Conversions._

  val repository = new FileBasedScheduleRepository()
  var isScheduleChanged = false
  var shouldSaveSchedule = true

  var file: File = null

  var schedule = Schedule()
  var schedulePane = new SchedulePane(schedule2Grid(schedule, this))
  val fileChooser = new FileChooser()

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
    saveSchedule(file, schedulePane)
  })
  saveMenuItem.enabled = false

  val openMenuItem = new MenuItem(Action("Open") {
    if (fileChooser.showOpenDialog(schedulePane) == FileChooser.Result.Approve) {
      file = fileChooser.selectedFile
      schedule = repository.findBy(FileScheduleSpec(file))
      schedulePane = new SchedulePane(schedule2Grid(schedule, this))
      saveMenuItem.enabled = true
      boxPanel.contents.update(0, schedulePane)
      boxPanel.revalidate()
    }
  })


  def top = {

    new MainFrame {
      title = "Domain Language Class Scheduler"

      menuBar = new MenuBar {
        contents += new Menu("File") {
          contents += openMenuItem
          contents += saveMenuItem
          contents += new Separator
          contents += new MenuItem(Action("Quit") {
            showCloseDialog()
            println("quitting")
          })
        }
      }

      listenTo(addEntryButton, removeEntryButton, this)
      reactions += {
        case ButtonClicked(`addEntryButton`) => schedulePane.addEmptyRow()
        case ButtonClicked(`removeEntryButton`) => schedulePane.removeSelectedRow()
      }

      contents = boxPanel

      private def showCloseDialog() {
        println("Showing close dialog")
        Dialog.showConfirmation(parent = null,
          title = "Exit",
          message = "Save schedule before exit?",
          optionType = Dialog.Options.YesNoCancel
        ) match {
          case Dialog.Result.Yes =>
            println("YES")
            saveSchedule(file, schedulePane)
            super.closeOperation()
          case Dialog.Result.No =>
            println("NO")
            super.closeOperation()
          case _ => ()
        }
      }
    }
  }


  def saveSchedule(file: File, schedulePane: SchedulePane) {
    if (file != null && isScheduleChanged) {
      repository.save(file, grid2Schedule(schedulePane.table.model.asInstanceOf[Grid]))
      isScheduleChanged = false
    }
  }

  def tableChanged(event: TableModelEvent): Unit = {
    println(s"Notified of table change: $event")
    isScheduleChanged = true
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