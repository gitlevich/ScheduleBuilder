package com.domainlanguage.schedule

import java.io.File

/**
 * User: Vladimir Gitlevich
 * Date: 10/12/13
 * Time: 16:50
 */
trait ScheduleExporter extends FilePersistence {
  private val detailTableFileName = "class-schedule-detail.php"

  private val detailTopTemplate = readFromClasspath("templates/events-page-header.html")
  private val detailCountryTemplate = readFromClasspath("templates/events-page-country.html")
  private val detailEventTemplate = readFromClasspath("templates/events-page-event-row.html")
  private val detailBottomTemplate = readFromClasspath("templates/events-page-footer.html")
  
  private val briefHeaderTemplate = readFromClasspath("templates/brief-header.html")
  private val briefContentsTemplate = readFromClasspath("templates/brief-contents.html")
  private val briefFooterTemplate = readFromClasspath("templates/brief-footer.html")

  def exportHtml(schedule: Schedule, destinationDir: File) {
    require(destinationDir.isDirectory)

    val sorted = schedule.sorted

    writeToFile(new File(destinationDir, detailTableFileName), asEventsPage(sorted))

    sorted.eventNames.foreach {
      theName =>
        writeToFile(new File(destinationDir, makeBriefPageFileName(theName)), asBriefsPage(theName, sorted))
    }
  }

  def asBriefsPage(theName: String, schedule: Schedule): String = {
    val briefsPage = new StringBuilder()
    briefsPage.append(briefHeaderTemplate)

    render(
      briefContentsTemplate, schedule.eventsByName(theName) map (event => eventSubstitutions(event))).
      foreach(line => briefsPage.append(line))

    briefsPage.append(briefFooterTemplate)

    briefsPage.toString()
  }

  def asEventsPage(schedule: Schedule): String = {
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
    List(Map(Schedule.region -> theCountry))

  private def eventSubstitutions(entry: Event): Map[String, String] = {
    import Schedule._
    Map(
      region -> ecs(entry.region),
      state -> ecs(entry.state),
      city -> ecs(entry.city),
      date -> ecs(entry.date),
      instructor -> ecs(entry.instructor),
      eventName -> ecs(entry.eventName),
      pricing -> ecs(entry.pricing),
      bookingPrompt -> s"${ecs(entry.bookingPrompt)}&nbsp;&raquo;",
      bookingUrl -> ecs(entry.bookingUrl)
    )
  }

  private def makeBriefPageFileName(name: String): String = name.toLowerCase.replaceAll(" ", "-")+"-brief.php"

  private def render(template: String, replacements: List[Map[String, String]]): List[String] =
    replacements.map (r => r.foldLeft(template)((s: String, x: (String, String)) =>
      ("#\\{" + x._1 + "\\}").r.replaceAllIn(s, x._2)))

  private def ecs(string: String): String =
    string.replaceAll("\\$", "&#36;").replace("£", "&#163;").replace("»", "&raquo;")

}


