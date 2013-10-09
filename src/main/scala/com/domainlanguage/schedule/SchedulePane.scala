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
          xAlignment = Alignment.Right
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

  def removeTableModelListener(listener: TableModelListener): Unit = {
    listeners -= listener
  }

  def addTableModelListener(listener: TableModelListener): Unit = {
    listeners += listener
  }

  def setValueAt(value: scala.Any, x: Int, y: Int): Unit =
    value match {
      case v: String => cells(x)(y) = TextCell(v)
      case v: Date => cells(x)(y) = DateCell(v)
    }

  def getRowCount: Int = schedule.entryCount

  def getColumnCount: Int = Row.columnNames.length

  def getColumnName(y: Int): String = Row.columnNames(y)

  def getColumnClass(y: Int): Class[_] = Row.columnTypes(y)

  def isCellEditable(x: Int, y: Int): Boolean = true

  def getValueAt(x: Int, y: Int): AnyRef = {
    cells(x)(y) match {
      case v: TextCell => v.text
      case v: DateCell => new SimpleDateFormat("mm/dd/yyyy").format(v.date)
    }
  }
}

case class Row(entry: ScheduleEntry) {
  val cells: Array[Cell] =
    Array(
      TextCell(entry.country),
      TextCell(entry.city),
      DateCell(entry.date),
      TextCell(entry.instructor),
      TextCell(entry.entryName),
      TextCell(entry.pricing),
      TextCell(entry.bookingPrompt),
      TextCell(entry.bookingUrl)
    )
}

object Row {
  val columnNames: Array[String] = Array("Country", "City", "Date", "Instructor", "Entry Name", "Pricing", "Booking Prompt", "Booking link")
  val columnTypes: Array[Class[_]] = Array(TextCell.getClass, TextCell.getClass, DateCell.getClass, TextCell.getClass, TextCell.getClass, TextCell.getClass, TextCell.getClass, TextCell.getClass)
  val width: Int = columnNames.length
}

sealed trait Cell
case class TextCell(text: String) extends Cell
case class DateCell(date: Date) extends Cell