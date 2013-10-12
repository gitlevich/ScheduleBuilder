package com.domainlanguage.schedule

import org.scalatest.junit.ShouldMatchersForJUnit
import org.junit.Test

/**
 * User: Vladimir Gitlevich
 * Date: 10/10/13
 * Time: 23:48
 */
class TemplateTest extends ShouldMatchersForJUnit {

  val template = "Hello #{name} and welcome to #{where}!"
  val replacements = Map( "name" -> "Vlad", "where" -> "the psychiatric helpline" )

  @Test def shouldBeInteresting() {
    val result = replacements.foldLeft(template)((s:String, x:(String,String)) => ( "#\\{" + x._1 + "\\}" ).r.replaceAllIn( s, x._2 ))

    result should equal ("Hello Vlad and welcome to the psychiatric helpline!")
  }

}
