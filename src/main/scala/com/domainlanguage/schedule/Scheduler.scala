package com.domainlanguage.schedule

import scala.swing._

/**
 * User: Vladimir Gitlevich
 * Date: 10/8/13
 * Time: 23:27
 */
object Scheduler extends SimpleSwingApplication {
  val repository = new FileBasedScheduleRepository()

  def top = new MainFrame {
    title = "Domain Language Class Scheduler"

    val schedule = repository.findByName("schedule.json")

    val grid = Grid(ScheduleEntry.columnNames)

    contents = new SchedulePane(grid)
  }
}
