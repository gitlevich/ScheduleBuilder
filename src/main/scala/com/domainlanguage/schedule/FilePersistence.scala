package com.domainlanguage.schedule

import java.io._
import scala.io.{Codec, Source}
import grizzled.slf4j.Logging
import java.util.Properties
import org.apache.commons.io.FileUtils

/**
 * User: Vladimir Gitlevich
 * Date: 10/11/13
 * Time: 17:25
 */
trait FilePersistence extends Logging {
  private val utf8 = "UTF-8"
  implicit val codec = Codec(utf8)

  def writeToFile(file: File, string: String) {
    FileUtils.writeStringToFile(file, string, utf8)
  }

  def readFromFile(file: File): String =
    withFileSource[String](file) { source => source.mkString }

  def readFromClasspath(fileName: String): String =
    withClassPathSource[String](fileName) { source => source.mkString }

  def loadProperties(file: File): Properties = {
    withFileReader[Properties](file)  {
      reader =>
        val props = new Properties()
        props.load(reader)
        props
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

  private def withFileReader[T](file: File)(op: FileReader => T): T = {
    val reader = new FileReader(file)
    try {
      op(reader)
    }
    finally {
      reader.close()
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
