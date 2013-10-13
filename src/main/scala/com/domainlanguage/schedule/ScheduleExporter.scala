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
    import Schedule._
    val replacements = schedule.events map {
      entry =>
        Map(country -> ecs(entry.country),
          city -> ecs(entry.city),
          date -> ecs(entry.date),
          instructor -> ecs(entry.instructor),
          eventName -> ecs(entry.eventName),
          pricing -> ecs(entry.pricing),
          bookingPrompt -> ecs(entry.bookingPrompt),
          bookingUrl -> ecs(entry.bookingUrl)
        )
    }

    val result = render(detailRow, replacements)

    val sb = new StringBuilder()
    sb.append(detailTop)
    result.foreach(r => sb.append(r))
    sb.append(detailBottom)

    sb.toString()
  }


  private def render(template: String, replacements: List[Map[String, String]]): List[String] =
    replacements.map (r => r.foldLeft(template)((s: String, x: (String, String)) =>
      ("#\\{" + x._1 + "\\}").r.replaceAllIn(s, x._2)))

  private def ecs(string: String): String =
    string.replaceAll("\\$", "&#36;").replace("£", "&#163;").replace("»", "&raquo;")

}
