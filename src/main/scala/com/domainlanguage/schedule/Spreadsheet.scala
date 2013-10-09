package com.domainlanguage.schedule

import scala.swing._
import java.util.Date

/**
 * User: Vladimir Gitlevich
 * Date: 10/8/13
 * Time: 23:29
 */

class Spreadsheet(schedule: Schedule) extends ScrollPane {
  val rowData = Array(Row(0, schedule.entries(0)).cells().asInstanceOf[Array[Any]])

  val table = new Table(rowData, Row.columnNames) {
    rowHeight = 25
    autoResizeMode = Table.AutoResizeMode.Off
    showGrid = true
    gridColor = new java.awt.Color(150, 150, 150)
  }

  val rowHeader =
    new ListView((1 until schedule.entryCount+1) map (_.toString)) {
      fixedCellWidth = 30
      fixedCellHeight = table.rowHeight
    }

  viewportView = table
  rowHeaderView = rowHeader
}


case class Row(y: Int, entry: ScheduleEntry) {
  def cells(): Array[Cell] =
    Array(
      TextCell(entry.country, 0, y),
      TextCell(entry.city, 1, y),
      DateCell(entry.date, 2, y),
      TextCell(entry.instructor, 3, y),
      TextCell(entry.entryName, 4, y),
      TextCell(entry.pricing, 5, y),
      TextCell(entry.bookingPrompt, 6, y),
      TextCell(entry.bookingUrl, 7, y)
    )
}

object Row {
  val columnNames = Array("Country", "City", "Date", "Instructor", "Entry Name", "Pricing", "Booking Prompt", "Booking link")
}

sealed trait Cell
case class TextCell(text: String, x: Int, y: Int) extends Cell
case class DateCell(date: Date, x: Int, y: Int) extends Cell