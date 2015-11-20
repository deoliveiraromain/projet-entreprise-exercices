package controllers


import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._
import reactivemongo.bson.BSONObjectID
import org.joda.time.DateTime
import play.api.libs.Files.TemporaryFile
import scala.io.Source
import scala.Predef._
import scala.Some
import models._

object ExerciceControllerBdd extends Controller {

  val creationForm = Form(
    mapping(
      "_id" -> ignored[Option[BSONObjectID]](None),
      "inputtitle" -> nonEmptyText,
      "inputmodule" -> nonEmptyText,
      "inputmatiere" -> nonEmptyText,
      "chapitre" -> optional(of[String]),
      "motcles" -> optional(of[String]),
      "inputtype"  -> nonEmptyText,
      "inputduree"  -> optional(of[String]),
      "contenu"  -> nonEmptyText,
      "corrige" -> optional(list(of[String]))
    ){
      (_id,inputtitle,inputmodule, inputmatiere,chapitre, motcles, inputtype, intputduree,contenu, listCorrige)
      => Exercice(Some(BSONObjectID.generate),
        inputtitle,
        inputmodule,
        inputmatiere,
        chapitre,
        motcles,
        inputtype,
        intputduree,
        new DateTime(),
        new DateTime(),
        contenu,
        listCorrige,
        None)
    } {
      exo
      => Some(exo._id,
        exo.titre,
        exo.module,
        exo.matiere,
        exo.chapitre,
        exo.motsCles,
        exo.typeExercice,
        exo.duree,
        exo.contenu.mkString(""),
        exo.corrige)
    }
  )



  /*Soumission du formulaire de création d'un  d'exo*/
  def submit = Action.async {
    implicit request =>
      creationForm.bindFromRequest.fold(
        formWithErrors => {
          Future.successful(BadRequest(views.html.creation(formWithErrors)))
        },
        exo => {
            Exercice.insert(exo)
            Future.successful(Redirect(routes.ExerciceControllerBdd.creation).flashing("success" -> "L'exercice a été crée avec succès !"))
        }
      )
  }

  /*Formulaire vide */
  def creation() = Action.async {
    implicit request =>
      if(request.flash.get("success").isDefined) {
        Exercice.findNewest().map {
          case Some(exo) =>
            Ok(views.html.creation(creationForm.fill(exo)))
          case None =>
            Redirect(routes.ExerciceControllerBdd.creation).flashing("error" -> "problème avec la base de donnée !")
        }
      }
      else {
        Future.successful(Ok(views.html.creation(creationForm)))
      }
  }
  /**
   * Appel de la page pour mettre à jour son exercice
   * @return Renvoie vers la page d'édition
   */
  def updateForm(idExercice :  String) = Action.async {
      implicit request =>
          Exercice.findById(idExercice).map {
          (current: Option[Exercice]) => current match {
            case Some(exercice) =>
              Ok(views.html.update(creationForm.fill(exercice),idExercice))
            case None =>
              NotFound("L'exercice n'existe pas")
          }
        }
  }

  def update(idExercice :  String) = Action.async {
    implicit request =>
      creationForm.bindFromRequest.fold(
        formWithErrors => {
          Future.successful(BadRequest(views.html.update(formWithErrors, idExercice)))
        },
        exo => {
          Exercice.update(idExercice,exo).map {
            (exerciceUpdated: Exercice) => Redirect(routes.ExerciceControllerBdd.consultation)
          }
        }
      )
  }


  def delete(id: String) = Action.async {
    Exercice.delete(id).map { _ =>
      Redirect(routes.ExerciceControllerBdd.consultation).flashing("deleted" -> "L'exercice a bien été supprimé !")
    }
  }




  def consultation() = Action.async {
    implicit request =>
      val futureExosList: Future[List[Exercice]] = Exercice.findAll()
      futureExosList.flatMap { (exos: List[Exercice]) =>
        val futureFeuillesList = Feuille.findAll()
        futureFeuillesList.map ( feuilles  =>  Ok(views.html.consultationexercices(exos,feuilles, uploadCorrigeForm)) )
      }
  }


  val uploadCorrigeForm = Form(
    "idexo" -> text
  )



  def addCorrige(idExercice :  String) = Action.async {
    implicit request =>
      Exercice.findById(idExercice).map {
        (current: Option[Exercice]) => current match {
          case Some(exercice) => Logger.debug("corrigé à éditer : "+exercice)
            Ok(views.html.corrige(creationForm.fill(exercice),idExercice))
          case None => Logger.debug(s"l'exercice n'existe pas !")
            NotFound("L'exercice n'existe pas")
        }
      }
  }

  def uploadCorrigeSubmit = Action.async(parse.multipartFormData){
    implicit request =>
      val idExo = uploadCorrigeForm.bindFromRequest.get
      request.body.file("inputfile").map{
        (fichier: MultipartFormData.FilePart[TemporaryFile]) =>
          val content = Source.fromFile(fichier.ref.file,"UTF-8").getLines().toList
          val firstCorrigeContent = LatexParser.getFirstCorrigeContent(content)
          Exercice.addCorrection(idExo, firstCorrigeContent,-1)
          Future.successful(Redirect(routes.ExerciceControllerBdd.consultation).flashing("updatedcorrige" -> "Le corrigé a été uploadé avec succès !"))
      }.getOrElse{
        Future.successful(Redirect(routes.ExerciceControllerBdd.consultation).flashing("error" -> "Le fichier n'a pu être traité"))
      }
  }

  def removeCorrige(idExercice : String, idCorrige : String) = Action.async {
    Exercice.removeCorrige(idExercice : String, idCorrige.split('_')(1).toInt).map {
      _ => Redirect(routes.ExerciceControllerBdd.updateForm(idExercice))
    }
  }

}



