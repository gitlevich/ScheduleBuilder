package com.domainlanguage.schedule

import scala.swing._
import java.util.Date
import javax.swing.table.TableModel
import javax.swing.event.TableModelListener
import scala.collection.mutable.ArrayBuffer
import java.text.SimpleDateFormat

/**
 * User: Vladimir Gitlevich
 * Date: 10/8/13
 * Time: 23:29
 */

class SchedulePane(spreadsheet: Spreadsheet) extends ScrollPane {

  val table = new Table(spreadsheet.cells.asInstanceOf[Array[Array[Any]]], Row.columnNames) {
    model = spreadsheet
    rowHeight = 25
    autoResizeMode = Table.AutoResizeMode.Off
    showGrid = true
    gridColor = new java.awt.Color(150, 150, 150)

    override def rendererComponent(isSelected: Boolean, hasFocus: Boolean, row: Int, column: Int): Component =
      if (hasFocus) new TextField(spreadsheet.getValueAt(row, column).asInstanceOf[String])
      else
        new Label(userData(row, column)) {
          xAlignment = Alignment.Left
        }

    def userData(row: Int, column: Int): String = {
      val v = this(row, column)
      if (v == null) "" else v.toString
    }
  }

  val rowHeader =
    new ListView((1 until spreadsheet.height+1) map (_.toString)) {
      fixedCellWidth = 30
      fixedCellHeight = table.rowHeight
    }

  viewportView = table
  rowHeaderView = rowHeader
}


case class Spreadsheet(schedule: Schedule) extends TableModel {
  val height = schedule.entryCount
  val width = Row.columnNames.length
  
  private val listeners: ArrayBuffer[TableModelListener] = ArrayBuffer.empty

  val cells: Array[Array[Cell]] = schedule.entries.map(entry => Row(entry).cells).toArray

  def removeTableModelListener(listener: TableModelListener): Unit =
    listeners -= listener

  def addTableModelListener(listener: TableModelListener): Unit =
    listeners += listener

  def setValueAt(value: scala.Any, x: Int, y: Int): Unit =
      cells(x)(y).text = value.asInstanceOf[String]

  def getRowCount: Int = schedule.entryCount

  def getColumnCount: Int = Row.columnNames.length

  def getColumnName(y: Int): String = Row.columnNames(y)

  def getColumnClass(y: Int): Class[_] = classOf[String]

  def isCellEditable(x: Int, y: Int): Boolean = true

  def getValueAt(x: Int, y: Int): AnyRef = cells(x)(y).text
}

case class Row(entry: ScheduleEntry) {
  val cells: Array[Cell] =
    Array(
      Cell(entry.country),
      Cell(entry.city),
      Cell(entry.date),
      Cell(entry.instructor),
      Cell(entry.entryName),
      Cell(entry.pricing),
      Cell(entry.bookingPrompt),
      Cell(entry.bookingUrl)
    )
}

object Row {
  val columnNames: Array[String] = Array("Country", "City", "Date", "Instructor", "Entry Name", "Pricing", "Booking Prompt", "Booking link")
  val width: Int = columnNames.length
}

case class Cell(var text: String)