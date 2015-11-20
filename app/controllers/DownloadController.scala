package controllers

import play.api.mvc._
import play.api.Play
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.Play.current

object DownloadController extends Controller {

  def downloadPdf = Action{
    val pdfFile = Play.application.getFile("public/generation/generatedTex.pdf")
    val source = scala.io.Source.fromFile(pdfFile)(scala.io.Codec.ISO8859)
    val byteArray = source.map(_.toByte).toArray
    source.close()
    Ok(byteArray).as("application/pdf")
  }
}