package controllers

import play.api.mvc._
import models._
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._
import scala.concurrent.Future
import reactivemongo.bson.BSONObjectID
import play.api.libs.Files.TemporaryFile
import scala.io.Source
import org.joda.time.DateTime
import scala.Some


object UploadController extends Controller {



  val uploadForm =Form(
    mapping(
      "_id" -> ignored[Option[BSONObjectID]](None),
      "inputtitle"-> nonEmptyText,
      "inputmodule" -> nonEmptyText,
      "inputmatiere" -> nonEmptyText,
      "chapitre" -> optional(of[String]),
      "motcles" -> optional(of[String]),
      "corrige" -> ignored[Option[List[String]]](None)
    ){
      (_id,inputtitle,inputmodule, inputmatiere,chapitre, motcles,corrige)
      => Exercice(Some(BSONObjectID.generate),
        inputtitle,
        inputmodule,
        inputmatiere,
        chapitre,
        motcles,
        "premiertype",
        Some("0 h 00 min"),
        new DateTime(),
        new DateTime(),
        "premierContenu",
        None,
        None)
    } {
      exo
      => Some(exo._id,
        exo.titre,
        exo.module,
        exo.matiere,
        exo.chapitre,
        exo.motsCles,
        exo.corrige)
    }
  )



  def upload = Action {
    implicit request =>
      Ok(views.html.upload(uploadForm))
  }

  def uploadsubmit = Action.async(parse.multipartFormData) {
    implicit request =>

      uploadForm.bindFromRequest.fold(

        formWithErrors => {
          Future.successful(BadRequest(views.html.upload(formWithErrors)))
        },
        exo => {
          request.body.file("inputfile").map {
            (fichier: MultipartFormData.FilePart[TemporaryFile]) =>

              val content=Source.fromFile( fichier.ref.file,"UTF-8").getLines().toList
              val mapOfExercisesContent: Map[String, String] = LatexParser.updateExercisesContentWithHTML(content)
              mapOfExercisesContent.keys.foreach{contenu =>
                val exerciceForme =
                  Exercice(Some(BSONObjectID.generate),
                    exo.titre,
                    exo.module,
                    exo.matiere,
                    exo.chapitre,
                    exo.motsCles,
                    mapOfExercisesContent(contenu),
                    exo.duree,
                    exo.dateCreation,
                    exo.dateModification,
                    contenu,
                    None,
                    None)
                  Exercice.insert(exerciceForme)
              }

              Future.successful(Redirect(routes.UploadController.upload).flashing("success" -> "Fichier tex uploadé en BDD"))

          }.getOrElse {
            Future.successful(Redirect(routes.UploadController.upload).flashing("error" -> "Missing file"))
          }
        }
      )

  }



}
