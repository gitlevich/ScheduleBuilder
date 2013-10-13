package com.domainlanguage.schedule

import java.io._
import scala.io.{Codec, Source}
import grizzled.slf4j.Logging

/**
 * User: Vladimir Gitlevich
 * Date: 10/11/13
 * Time: 17:25
 */
trait FilePersistence extends Logging {
  implicit val codec = Codec("UTF-8")

  def writeToFile(file: File, string: String) {
    withPrintWriter(file) { writer => writer.println(string)}
  }

  def readFromFile(file: File): String = {
    require(file != null)
    withFileSource[String](file) { source => source.mkString }
  }

  def readFromClasspath(fileName: String): String = {
    require(fileName != null)
    withClassPathSource[String](fileName) { source => source.mkString }
  }

  private def withPrintWriter(file: File)(op: PrintWriter => Unit) {
    val writer = new PrintWriter(file)
    try {
      op(writer)
    }
    finally {
      writer.close()
    }
  }

  private def withFileSource[T](file: File)(op: Source => T): T = {
    val source = Source.fromFile(file)
    try {
      op(source)
    }
    finally {
      source.close()
    }
  }

  private def withClassPathSource[T](fileName: String)(op: Source => T): T = {
    val source = Source.fromInputStream(classOf[FileBasedScheduleRepository].getClassLoader.getResourceAsStream(fileName))
    try {
      op(source)
    }
    finally {
      source.close()
    }
  }

}
