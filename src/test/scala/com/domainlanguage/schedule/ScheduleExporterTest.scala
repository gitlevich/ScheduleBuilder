package com.domainlanguage.schedule

import org.scalatest.junit.ShouldMatchersForJUnit
import org.junit.Test

/**
 * User: Vladimir Gitlevich
 * Date: 10/10/13
 * Time: 23:48
 */
class ScheduleExporterTest extends ShouldMatchersForJUnit with ScheduleExporter {


  @Test def shouldProduceEventDetailsPage() {
    val schedule = new FileBasedScheduleRepository().findBy(ClassPathScheduleSpec("schedule.json"))

    val actual = asEventsPage(schedule)
    println(actual)
    actual should equal (expectedDetailPage)
  }

  @Test def shouldProduceEventBriefsPage() {
    val schedule = new FileBasedScheduleRepository().findBy(ClassPathScheduleSpec("schedule.json"))

    val actual = asBriefsPage("DDD Overview",schedule)
    println(actual)
    actual should equal (expectedBriefPage)
  }

  private val expectedDetailPage = """<h1>Schedule and Pricing of Public Training</h1>
                                     |<table id="schedule" cellpadding="3">
                                     |<tr>
                                     |    <td colspan="6">
                                     |        <h2>UK</h2>
                                     |    </td>
                                     |</tr>
                                     |<tr>
                                     |    <td nowrap>London</td>
                                     |    <td width="10%"><a href="http://domainlanguage.com/training/schedule/DDDfLON-P">Oct 28</a></td>
                                     |    <td width="15%">Alberto Brandolini</td>
                                     |    <td width="15%"><a href="http://domainlanguage.com/training/schedule/DDDfLON-P">DDD Foundations</a></td>
                                     |    <td><strike>&#163;1195</strike> &#163;1075 60 days prior</td>
                                     |    <td><a href="http://domainlanguage.com/training/schedule/DDDfLON-P">Book Online&nbsp;&raquo;</a></td>
                                     |</tr>
                                     |<tr><td colspan="6"><hr></td></tr>
                                     |<tr style="height:1px;">
                                     |    <td colspan="6">&nbsp;</td>
                                     |</tr>
                                     |<tr>
                                     |    <td colspan="6">
                                     |        <h2>NORTH AMERICA</h2>
                                     |    </td>
                                     |</tr>
                                     |<tr>
                                     |    <td nowrap>San Francisco</td>
                                     |    <td width="10%"><a href="http://domainlanguage.com/training/schedule/DDDoNYC2013-11-15C">Nov 15</a></td>
                                     |    <td width="15%">Eric Evans</td>
                                     |    <td width="15%"><a href="http://domainlanguage.com/training/schedule/DDDoNYC2013-11-15C">DDD Overview</a></td>
                                     |    <td>See QCon SF site.</td>
                                     |    <td><a href="http://domainlanguage.com/training/schedule/DDDoNYC2013-11-15C">Book Online&nbsp;&raquo;</a></td>
                                     |</tr>
                                     |<tr>
                                     |    <td nowrap>New York</td>
                                     |    <td width="10%"><a href="http://domainlanguage.com/training/schedule/DDDmNYC2014-01-21P">Jan 21</a></td>
                                     |    <td width="15%">Eric Evans</td>
                                     |    <td width="15%"><a href="http://domainlanguage.com/training/schedule/DDDmNYC2014-01-21P">DDD Immersion</a></td>
                                     |    <td>&#36;3,090 &#36;2,790 if booked by 8/5</td>
                                     |    <td><a href="http://domainlanguage.com/training/schedule/DDDmNYC2014-01-21P">Book Online&nbsp;&raquo;</a></td>
                                     |</tr>
                                     |<tr>
                                     |    <td nowrap>New York</td>
                                     |    <td width="10%"><a href="http://domainlanguage.com/training/schedule/SD-NYC2014-01-27P">Jan 27</a></td>
                                     |    <td width="15%">Eric Evans</td>
                                     |    <td width="15%"><a href="http://domainlanguage.com/training/schedule/SD-NYC2014-01-27P">Strategic Design</a></td>
                                     |    <td>&#36;1,850 &#36;1,295 if booked by 12/20</td>
                                     |    <td><a href="http://domainlanguage.com/training/schedule/SD-NYC2014-01-27P">Book Online&nbsp;&raquo;</a></td>
                                     |</tr>
                                     |<tr><td colspan="6"><hr></td></tr>
                                     |<tr style="height:1px;">
                                     |    <td colspan="6">&nbsp;</td>
                                     |</tr>
                                     |<tr>
                                     |    <td colspan="6">
                                     |        <h2>AUSTRALIA</h2>
                                     |    </td>
                                     |</tr>
                                     |<tr>
                                     |    <td nowrap>Melbourne</td>
                                     |    <td width="10%"><a href="http://domainlanguage.com/training/schedule/DDDoYOW-C">Dec 03</a></td>
                                     |    <td width="15%">Eric Evans</td>
                                     |    <td width="15%"><a href="http://domainlanguage.com/training/schedule/DDDoYOW-C">DDD Overview</a></td>
                                     |    <td>See YOW site.</td>
                                     |    <td><a href="http://domainlanguage.com/training/schedule/DDDoYOW-C">Book through YOW&nbsp;&raquo;</a></td>
                                     |</tr>
                                     |<tr><td colspan="6"><hr></td></tr>
                                     |<tr style="height:1px;">
                                     |    <td colspan="6">&nbsp;</td>
                                     |</tr>
                                     |</table>
                                     |""".stripMargin

  private val expectedBriefPage = """<div class="schedule-summary">
                  |    <ul>
                  |        <li><a href="http://domainlanguage.com/training/schedule/DDDoNYC2013-11-15C">Nov 15, San Francisco, CA</a></li>
                  |        <li><a href="http://domainlanguage.com/training/schedule/DDDoYOW-C">Dec 03, Melbourne, Australia</a></li>
                  |    </ul>
                  |</div>
                  |""".stripMargin
}
