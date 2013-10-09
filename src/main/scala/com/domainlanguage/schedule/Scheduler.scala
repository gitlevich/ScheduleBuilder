package com.domainlanguage.schedule

import scala.swing._
import java.util.Date

/**
 * User: Vladimir Gitlevich
 * Date: 10/8/13
 * Time: 23:27
 */
object Scheduler extends SimpleSwingApplication {
  def top = new MainFrame {
    title = "Domain Language Class Scheduler"

    val schedule = Schedule().withNewEntry(
      ScheduleEntry("North America", "San Francisco", new Date(), "Eric Evans", "DDD Overview", "See QCon SF site.", "Book Online", "/some/where" ))

    contents = new Spreadsheet(schedule)
  }
}
