package com.domainlanguage.schedule

import scala.swing._

/**
 * User: Vladimir Gitlevich
 * Date: 10/8/13
 * Time: 23:29
 */

class SchedulePane(grid: Grid) extends ScrollPane {

  var selectedRow: Option[Int] = None

  val table = new Table(grid.rows.asInstanceOf[Array[Array[Any]]], grid.columnHeaders) {
    model = grid
    rowHeight = 25
    autoResizeMode = Table.AutoResizeMode.LastColumn
    showGrid = true
    gridColor = new java.awt.Color(150, 150, 150)

    override def rendererComponent(isSelected: Boolean, hasFocus: Boolean, row: Int, column: Int): Component =
      if (hasFocus) {
        selectedRow = Option(row)
        new TextField(grid.getValueAt(row, column).asInstanceOf[String])
      }
      else
        new Label(userData(row, column)) {
          xAlignment = Alignment.Left
        }

    private def userData(row: Int, column: Int): String = {
      val v = this(row, column)
      if (v == null) "" else v.toString
    }
  }

  def removeSelectedRow(): Unit = {
    selectedRow map( selection => grid.removeRow(selection))
    updateRowHeader()
    revalidate()
  }

  def addEmptyRow(): Unit = {
    grid.addEmptyRow()
    updateRowHeader()
    revalidate()
  }


  private def generateRowHeader() =
    new ListView((1 until grid.getRowCount+1) map (_.toString)) {
      fixedCellWidth = 30
      fixedCellHeight = table.rowHeight
    }

  private def updateRowHeader(): Unit = {
    rowHeaderView = generateRowHeader()
  }

  viewportView = table
  rowHeaderView = generateRowHeader()
}
