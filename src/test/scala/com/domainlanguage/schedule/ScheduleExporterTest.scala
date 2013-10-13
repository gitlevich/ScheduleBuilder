package com.domainlanguage.schedule

import org.scalatest.junit.ShouldMatchersForJUnit
import org.junit.Test

/**
 * User: Vladimir Gitlevich
 * Date: 10/10/13
 * Time: 23:48
 */
class ScheduleExporterTest extends ShouldMatchersForJUnit with ScheduleExporter {


  @Test def shouldBeInteresting() {
    val schedule = new FileBasedScheduleRepository().findBy(ClassPathScheduleSpec("schedule.json"))

    val actual = asDetailTable(schedule)
    actual should equal (expected)
  }

  val expected = """<h1>Schedule and pricing of Public Training</h1>
                   |<table id="schedule">
                   |<tr>
                   |    <td>NORTH AMERICA</td>
                   |    <td>San Francisco</td>
                   |    <td><a href="http://domainlanguage.com/training/schedule/DDDoNYC2013-11-15C">Nov 15, 2013</a></td>
                   |    <td>Eric Evans</td>
                   |    <td><a href="http://domainlanguage.com/training/schedule/DDDoNYC2013-11-15C">DDD Overview</a></td>
                   |    <td>See QCon SF site.</td>
                   |    <td><a href="http://domainlanguage.com/training/schedule/DDDoNYC2013-11-15C">Book Online&nbsp;&raquo;</a></td>
                   |</tr>
                   |<tr>
                   |    <td width="15%">&nbsp;</td>
                   |    <td width="10%">&nbsp;</td>
                   |    <td width="15%">&nbsp;</td>
                   |    <td width="15%">&nbsp;</td>
                   |</tr>
                   |<tr>
                   |    <td>NORTH AMERICA</td>
                   |    <td>New York</td>
                   |    <td><a href="http://domainlanguage.com/training/schedule/DDDmNYC2014-01-21P">Jan 21, 2014</a></td>
                   |    <td>Eric Evans</td>
                   |    <td><a href="http://domainlanguage.com/training/schedule/DDDmNYC2014-01-21P">DDD Immersion</a></td>
                   |    <td>&#36;3,090 &#36;2,790 if booked by 8/5</td>
                   |    <td><a href="http://domainlanguage.com/training/schedule/DDDmNYC2014-01-21P">Book Online&nbsp;&raquo;</a></td>
                   |</tr>
                   |<tr>
                   |    <td width="15%">&nbsp;</td>
                   |    <td width="10%">&nbsp;</td>
                   |    <td width="15%">&nbsp;</td>
                   |    <td width="15%">&nbsp;</td>
                   |</tr>
                   |<tr>
                   |    <td>NORTH AMERICA</td>
                   |    <td>New York</td>
                   |    <td><a href="http://domainlanguage.com/training/schedule/SD-NYC2014-01-27P">Jan 27, 2014</a></td>
                   |    <td>Eric Evans</td>
                   |    <td><a href="http://domainlanguage.com/training/schedule/SD-NYC2014-01-27P">Strategic Design</a></td>
                   |    <td>&#36;1,850 &#36;1,295 if booked by 12/20</td>
                   |    <td><a href="http://domainlanguage.com/training/schedule/SD-NYC2014-01-27P">Book Online&nbsp;&raquo;</a></td>
                   |</tr>
                   |<tr>
                   |    <td width="15%">&nbsp;</td>
                   |    <td width="10%">&nbsp;</td>
                   |    <td width="15%">&nbsp;</td>
                   |    <td width="15%">&nbsp;</td>
                   |</tr>
                   |<tr>
                   |    <td>AUSTRALIA</td>
                   |    <td>Melbourne</td>
                   |    <td><a href="http://domainlanguage.com/training/schedule/DDDoYOW-C">Dec 03, 2014</a></td>
                   |    <td>Eric Evans</td>
                   |    <td><a href="http://domainlanguage.com/training/schedule/DDDoYOW-C">DDD Overview</a></td>
                   |    <td>See YOW site.</td>
                   |    <td><a href="http://domainlanguage.com/training/schedule/DDDoYOW-C">Book through YOW&nbsp;&raquo;</a></td>
                   |</tr>
                   |<tr>
                   |    <td width="15%">&nbsp;</td>
                   |    <td width="10%">&nbsp;</td>
                   |    <td width="15%">&nbsp;</td>
                   |    <td width="15%">&nbsp;</td>
                   |</tr>
                   |<tr>
                   |    <td>UK</td>
                   |    <td>London</td>
                   |    <td><a href="http://domainlanguage.com/training/schedule/DDDfLON-P">Oct 28, 2013</a></td>
                   |    <td>Alberto Brandolini</td>
                   |    <td><a href="http://domainlanguage.com/training/schedule/DDDfLON-P">DDD Foundations</a></td>
                   |    <td><strike>&#163;1195</strike> &#163;1075 60 days prior</td>
                   |    <td><a href="http://domainlanguage.com/training/schedule/DDDfLON-P">Book Online&nbsp;&raquo;</a></td>
                   |</tr>
                   |<tr>
                   |    <td width="15%">&nbsp;</td>
                   |    <td width="10%">&nbsp;</td>
                   |    <td width="15%">&nbsp;</td>
                   |    <td width="15%">&nbsp;</td>
                   |</tr>
                   |</table>
                   |""".stripMargin

}
