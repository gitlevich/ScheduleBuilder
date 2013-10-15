package com.domainlanguage.schedule

import java.io.{StringWriter, PrintWriter, FileInputStream, File}
import org.apache.commons.net.ftp.{FTP, FTPClientConfig, FTPClient}
import grizzled.slf4j.Logging
import org.apache.commons.net.PrintCommandListener
import java.util.Properties

/**
 * User: Vladimir Gitlevich
 * Date: 10/15/13
 * Time: 0:21
 */
trait FTPUpload extends Logging {

  def upload(files: List[File], destinationSpec: FtpDestinationSpec): Option[String] = {
    val ftp = new FTPClient()
    val ftpMessages = new StringWriter
    try {
      val config = new FTPClientConfig()
      config.setLenientFutureDates(true)
      ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(ftpMessages)))
      ftp.configure(config)
      ftp.connect(destinationSpec.host)
      ftp.login(destinationSpec.login, destinationSpec.password)
      ftp.setFileType(FTP.ASCII_FILE_TYPE)
      ftp.enterLocalPassiveMode()

      files.foreach(file =>
        withInputStream(file) {
          stream =>
            if(destinationSpec.remoteDir != "/") {
              if(!ftp.listDirectories().map(dir => dir.getName).contains(destinationSpec.remoteDir)) {
                ftp.makeDirectory(destinationSpec.remoteDir)
              }
            }
            val ok = ftp.storeFile(s"${destinationSpec.remoteDir}${file.getName}", stream)
            if(!ok) throw new Exception(s"Unable to upload ${file.getAbsoluteFile} to ${destinationSpec.remoteDir}${file.getName}")
            debug(s"Uploaded file ${file.getAbsoluteFile} to ${destinationSpec.remoteDir}${file.getName}")
        }
      )

      debug(ftpMessages)
      None
    }
    catch {
      case e: Exception =>
        debug(ftpMessages)
        Some(e.getMessage)
    }
    finally {
      ftp.disconnect()
    }
  }

  private def withInputStream[T](file: File)(op: FileInputStream => T): T = {
    val stream = new FileInputStream(file)
    try {
      op(stream)
    }
    finally {
      stream.close()
    }
  }
}


case class FtpDestinationSpec(props: Properties) {
  val host = props.getProperty("serverName")
  val login = props.getProperty("login")
  val password = props.getProperty("password")
  val remoteDir = {
    val dir = props.getProperty("remoteDir")
    if(dir == null) {
      "/"
    }
    else {
      if(dir.endsWith("/")) dir
      else dir+"/"
    }
  }
}