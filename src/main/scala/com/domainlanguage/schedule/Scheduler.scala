package com.domainlanguage.schedule

import scala.swing._

/**
 * User: Vladimir Gitlevich
 * Date: 10/8/13
 * Time: 23:27
 */
object Scheduler extends SimpleSwingApplication {
  def top = new MainFrame {
    title = "Domain Language Class Scheduler"

    val schedule = Schedule()
      .withNewEntry(ScheduleEntry("North America", "San Francisco", "11/15/2013", "Eric Evans", "DDD Overview", "See QCon SF site.", "Book Online", "/some/where" ))
      .withNewEntry(ScheduleEntry("North America", "New York", "01/21/2014", "Eric Evans", "DDD Immersion", "$3,090 $2,790\nif booked by 8/5", "Book Online", "/some/where" ))

    contents = new SchedulePane(Spreadsheet(schedule))
  }
}
