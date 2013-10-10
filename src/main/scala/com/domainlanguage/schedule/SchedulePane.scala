package com.domainlanguage.schedule

import scala.swing._

/**
 * User: Vladimir Gitlevich
 * Date: 10/8/13
 * Time: 23:29
 */

class SchedulePane(grid: Grid) extends ScrollPane {

  val table = new Table(grid.cells.asInstanceOf[Array[Array[Any]]], grid.columnHeaders) {
    model = grid
    rowHeight = 25
    autoResizeMode = Table.AutoResizeMode.Off
    showGrid = true
    gridColor = new java.awt.Color(150, 150, 150)

    override def rendererComponent(isSelected: Boolean, hasFocus: Boolean, row: Int, column: Int): Component =
      if (hasFocus) new TextField(grid.getValueAt(row, column).asInstanceOf[String])
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
    new ListView((1 until grid.getRowCount+1) map (_.toString)) {
      fixedCellWidth = 30
      fixedCellHeight = table.rowHeight
    }

  viewportView = table
  rowHeaderView = rowHeader
}
