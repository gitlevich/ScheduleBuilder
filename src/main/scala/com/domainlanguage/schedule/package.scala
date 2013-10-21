package com.domainlanguage

import java.util.Date
import java.text.SimpleDateFormat

/**
 * User: Vladimir Gitlevich
 * Date: 10/17/13
 * Time: 1:03
 */
package object schedule {

  val DateFormat = "MMM dd, yyyy"

  implicit def stringToDate(string: String): Date = new SimpleDateFormat(DateFormat).parse(string)
  implicit def dateToString(date: Date): String = new SimpleDateFormat(DateFormat).format(date)

  implicit def optionDateToString(optDate: Option[Date]): String = optDate match {
    case Some(x) => x
    case None => ""
  }
  implicit def stringToOptionDate(dateString: String): Option[Date] = {
    try {
      Option(stringToDate(dateString))
    }
    catch {
      case e: Exception => None
    }
  }
}
