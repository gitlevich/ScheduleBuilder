package com.domainlanguage.schedule

import java.io.File

/**
 * User: Vladimir Gitlevich
 * Date: 10/12/13
 * Time: 16:50
 */
trait ScheduleExporter extends FilePersistence {
  private val detailTableFileName = "class-schedule-detail.html"

  private val detailTopTemplate = readFromClasspath("templates/events-page-header.html")
  private val detailCountryTemplate = readFromClasspath("templates/events-page-country.html")
  private val detailEventTemplate = readFromClasspath("templates/events-page-event-row.html")
  private val detailBottomTemplate = readFromClasspath("templates/events-page-footer.html")

  def exportDetailTable(schedule: Schedule, file: File) {
    require(file.isDirectory)

    writeToFile(new File(file, detailTableFileName), asDetailTable(schedule))
  }

  def asDetailTable(schedule: Schedule): String = {
    val eventsPage = new StringBuilder()
    eventsPage.append(detailTopTemplate)

    schedule.eventCountries.foreach {
      theCountry =>
        render(detailCountryTemplate, countrySubstitutions(theCountry)).
          foreach(line => eventsPage.append(line))

        render(
          detailEventTemplate, schedule.eventsByCountry(theCountry) map (event => eventSubstitutions(event))).
            foreach(line => eventsPage.append(line))
    }

    eventsPage.append(detailBottomTemplate)

    eventsPage.toString()
  }


  private def countrySubstitutions(theCountry: String): List[Map[String, String]] =
    List(Map(Schedule.country -> theCountry))

  private def eventSubstitutions(entry: Event): Map[String, String] = {
    import Schedule._
    Map(city -> ecs(entry.city),
      date -> ecs(entry.date),
      instructor -> ecs(entry.instructor),
      eventName -> ecs(entry.eventName),
      pricing -> ecs(entry.pricing),
      bookingPrompt -> ecs(entry.bookingPrompt),
      bookingUrl -> ecs(entry.bookingUrl)
    )
  }

  private def render(template: String, replacements: List[Map[String, String]]): List[String] =
    replacements.map (r => r.foldLeft(template)((s: String, x: (String, String)) =>
      ("#\\{" + x._1 + "\\}").r.replaceAllIn(s, x._2)))

  private def ecs(string: String): String =
    string.replaceAll("\\$", "&#36;").replace("£", "&#163;").replace("»", "&raquo;")

}
