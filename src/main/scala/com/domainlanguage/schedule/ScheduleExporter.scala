package com.domainlanguage.schedule

import java.io.File

/**
 * User: Vladimir Gitlevich
 * Date: 10/12/13
 * Time: 16:50
 */
trait ScheduleExporter extends FilePersistence {
  private val detailTableFileName = "class-schedule-detail.html"

  private val detailTop = readFromClasspath("detail-top.html")
  private val detailRow = readFromClasspath("detail-table-row.html")
  private val detailBottom = readFromClasspath("detail-bottom.html")

  def exportDetailTable(schedule: Schedule, file: File) {
    require(file.isDirectory)

    writeToFile(new File(file, detailTableFileName), asDetailTable(schedule))
  }

  def asDetailTable(schedule: Schedule): String = {
    val replacements = schedule.entries map {
      entry =>
        Map("country" -> entry.country,
          "city" -> entry.city,
          "date" -> entry.date,
          "instructor" -> entry.instructor,
          "entryName" -> entry.entryName,
          "pricing" -> entry.pricing,
          "bookingPrompt" -> entry.bookingPrompt,
          "bookingUrl" -> entry.bookingUrl
        )
    }

    val result = replacements.map {
      r => r.foldLeft(detailRow)((s: String, x: (String, String)) => ("#\\{" + x._1 + "\\}").r.replaceAllIn(s, x._2))
    }

    val sb = new StringBuilder()
    sb.append(detailTop)
    result.foreach(r => sb.append(r))
    sb.append(detailBottom)

    sb.toString()
  }
}
