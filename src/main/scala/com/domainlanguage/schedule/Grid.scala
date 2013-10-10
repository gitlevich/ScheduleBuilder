package com.domainlanguage.schedule

import scala.collection.mutable.ListBuffer
import javax.swing.table.TableModel
import javax.swing.event.{TableModelEvent, TableModelListener}

/**
 * User: Vladimir Gitlevich
 * Date: 10/9/13
 * Time: 15:19
 */
case class Grid(gridCells: ListBuffer[ListBuffer[GridCell]], columnHeaders: Array[String]) extends TableModel {
  def rows: Array[Array[GridCell]] = {
    val cellArray = Array.ofDim[GridCell](getRowCount, getColumnCount)

    for(row <- 0 until getRowCount; column <- 0 until getColumnCount) {
      cellArray(row)(column) = gridCells(row)(column)
    }

    cellArray
  }

  def addEmptyRow(): Unit = {
    gridCells += Grid.emptyRow
    println(gridCells)
    notifyListeners()
  }

  def removeRow(row: Int): Unit = {
    gridCells.remove(row)
    println(gridCells)
    notifyListeners()
  }

  def notifyListeners() {
    listeners.foreach(l => l.tableChanged(new TableModelEvent(this)))
  }

  def setValueAt(value: Any, row: Int, column: Int): Unit =
    gridCells(row)(column) = GridCell(value.asInstanceOf[String])
  def getValueAt(row: Int, column: Int): AnyRef = gridCells(row)(column).value

  def getRowCount: Int = gridCells.length
  def getColumnCount: Int = columnHeaders.length

  def getColumnName(column: Int): String = columnHeaders(column)
  def getColumnClass(column: Int): Class[_] = classOf[String]

  def isCellEditable(row: Int, column: Int): Boolean = true

  def removeTableModelListener(listener: TableModelListener): Unit =
    listeners -= listener

  def addTableModelListener(listener: TableModelListener): Unit =
    listeners += listener

  private val listeners: ListBuffer[TableModelListener] = ListBuffer.empty
}

object Grid {
  def emptyRow = ListBuffer(GridCell(), GridCell(), GridCell(), GridCell(), GridCell(), GridCell(), GridCell(), GridCell())
}

case class GridCell(value: String = "")
