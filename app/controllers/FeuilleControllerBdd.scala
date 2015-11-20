package controllers

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._
import reactivemongo.bson.BSONObjectID
import org.joda.time.DateTime
import scala.Some
import scala.collection.immutable.IndexedSeq
import models._

object FeuilleControllerBdd extends Controller  {



  def validationDuree(duree : Option[String]) : Boolean = {
    if (duree.isDefined){
      if(duree.get matches "^[0-9] h [0-9]+ min$")  {
        true
      }
      else {
        false
      }
    }
    else {
      true
    }
  }

  def validationClasse(classe :String, option: Option[String]) : Boolean ={
    if(classe == "ING3") {
      option.nonEmpty
      }
    else {
      option.isEmpty
      }
  }


  def validationNumerotation(listeExos : List[String]) : Boolean ={
   var res : Boolean =true
    if(listeExos.contains("")){
      res=false
    }
    else {
    val sumList: Int = listeExos.map(_.trim().toInt).foldLeft(0)(_ + _)
    val listeI: IndexedSeq[Int] = for{
      i <-1 to listeExos.length
    } yield {
        i
    }
    val sumIndex = listeI.foldLeft(0)(_ + _)
    if(sumList != sumIndex ){
        res=false
    }
    }
  res
  }



  //Formulaire permettant le mapping d'une feuille d'exo
  val creationForm = Form(
    mapping(

      "_id" -> optional (of [String]),
      "prof" -> optional(of[String]),
      "inputtitle" -> nonEmptyText,
      "inputmodule" -> nonEmptyText,
      "inputmatiere" -> nonEmptyText,
      "inputpromo" -> tuple(
        "classe" -> nonEmptyText,
        "option" ->optional(of[String])).verifying("Option non renseignée !", promo => validationClasse(promo._1, promo._2)),
      "carac"->tuple(
        "typefeuille" -> nonEmptyText,
        "tempsimparti" -> optional(of[String])),
      "realtempsimparti"->optional(of[String]).verifying("Durée mal renseignée !",text => validationDuree(text)),
      "dateexamen" -> optional(of[String]),
      "listeExos"  -> list(text).verifying("Aucun exercice choisi !",liste => liste.nonEmpty),
       "listeExosNumber" ->list(text).verifying("Numérotation mal renseignée", liste => validationNumerotation(liste))


      ) {
      (_id, prof, inputtitre,inputmodule,inputmatiere,inputpromo,carac,realtempsimparti,dateexamen,listeExos,listeExosNumber)
      =>
        //Portion de code pour mettre les exos dans l'ordre précisé par l'utilisateur au moment de la création de la feuille
        val array = new Array[String](listeExos.size)
        listeExos.zipWithIndex.foreach(
          el => {
            val positionDansLaFeuille: Int  = listeExosNumber(el._2).toInt
            array(positionDansLaFeuille-1) = el._1
          }
        )
        val newList = array.toList
        Feuille(Some(BSONObjectID(_id.getOrElse(BSONObjectID.generate.stringify))),
                 prof,
                 inputtitre,
                 inputmodule,
                 inputmatiere,
                 inputpromo._1,
                 inputpromo._2,
                 carac._1,
                 carac._2,
                 new DateTime(),
                 new DateTime(),
                 realtempsimparti,
                 dateexamen,
                newList.map(BSONObjectID(_)))
    } {
      feuille
      => Some(Some(feuille._id.get.stringify), feuille.prof,feuille.titre,feuille.module,feuille.matiere,(feuille.promo,feuille.option),(feuille.typefeuille,feuille.duree),feuille.realDuree,feuille.dateExamen,feuille.listeExos.map(_.stringify),List(""))
    }
  )


   /*Soumission du formulaire de création d'une feuille d'exo*/
   def submit = Action.async {
     implicit request =>

       creationForm.bindFromRequest.fold(
         formWithErrors => {
           val futureExosList: Future[List[Exercice]] = Exercice.findAll()
           futureExosList.flatMap{
             exos =>
             val futureFeuillesList = Feuille.findAll()
             futureFeuillesList.map(feuilles =>BadRequest(views.html.creationfeuille(formWithErrors,exos,feuilles,true)))
           }
         },
         feuille => {
          Feuille.insert(feuille)
          Future.successful(Redirect(routes.FeuilleControllerBdd.creationFeuilleMan).flashing("success" ->"La feuille a bien été crée !"))

          })
   }
  //Controller page de création
    def creationFeuilleMan = Action.async {
      implicit  request =>
        val futureExosList = Exercice.findAll()
          futureExosList.flatMap{
            exos =>
              val futureFeuillesList = Feuille.findAll()
              futureFeuillesList.map(feuilles =>Ok(views.html.creationfeuille(creationForm,exos,feuilles,true)))
        }
    }

  def creationFeuilleAuto = TODO

   //Controller page de consultation
  def consultation() = Action.async {
      val futureFeuillesList = Feuille.findAll()
      futureFeuillesList.flatMap { feuilles =>
        val futureExosList = Exercice.findAll()
        futureExosList.map{  exos=>
          Ok(views.html.consultationfeuilles(feuilles,exos))
        }
    }
  }

  /*Soumission du formulaire de mise à jour d'une feuille d'exo*/
  def updateForm = Action.async {
    implicit request =>
      creationForm.bindFromRequest.fold(
        formWithErrors => {
          val futureExosList = Exercice.findAll()
          futureExosList.flatMap{
            exos =>
              val futureFeuillesList = Feuille.findAll()
              futureFeuillesList.map(
            feuilles =>BadRequest(views.html.creationfeuille(formWithErrors,exos,feuilles,false)))
          }
        },
        feuille => {
          Feuille.update(feuille._id.get.stringify,feuille)
          Future.successful(Redirect(routes.FeuilleControllerBdd.creationFeuilleMan).flashing("success" ->"La feuille a bien été mise à jour !"))

        })
  }

  def update(feuilleId : String) = Action.async {
    implicit request =>
      Feuille.findById(feuilleId).flatMap {
        (current: Option[Feuille]) => current match {
          case Some(feuille) => {
            val futureExosList = Exercice.findAll()
            futureExosList.flatMap{
              exos =>
                val futureFeuillesList = Feuille.findAll()
                futureFeuillesList.map(
                  feuilles =>
                    Ok(views.html.creationfeuille(creationForm.fill(feuille),exos,feuilles,false)))
            }
          }
          case None =>
            Future.successful(NotFound("Feuille inexistante !"))
        }
      }

  }

  def delete(id: String) = Action.async {
    Feuille.delete(id)
    Future.successful(Redirect(routes.FeuilleControllerBdd.consultation).flashing("deleted"->"Feuille d'exercice supprimée !"))
  }

   def generation(id : String) = Action.async {
     Feuille.findById(id).flatMap{
     current =>
       current match {
         case Some(feuille) => {
           Exercice.findAll().map((exercices: List[Exercice]) => exercices.filter((exercice: Exercice) => feuille.listeExos.contains(exercice._id.get))).map {
           listeExos =>
             val listeExosF: List[List[Exercice]] = feuille.listeExos.map(id => {
               listeExos.filter(exo => id == exo._id.get)
             })
             generatePdf(feuille, listeExosF.flatten)
             Ok(views.html.visualisationfeuille(feuille,listeExosF.flatten))
           }
         }
         case None => {
           Future(Redirect(routes.FeuilleControllerBdd.consultation).flashing("notExisted" -> "La feuille à générer n'existe pas !"))
         }
     }
   }
   }

  def generatePdf(feuille:Feuille, exos: List[Exercice]) = {
    val listeContenuExos: List[String] = exos.map(exo => exo.contenu)
    var isExam = false
    val dateFeuille = feuille.dateModification.year().get()+"\\}"+"\\{"+feuille.dateModification.monthOfYear().get()+"\\}"+"\\{"+feuille.dateModification.dayOfMonth().get()
    if(feuille.typefeuille == "Examen"){
      isExam = true
    }
    val latexRes = HtmlParser.parseHtmlExercisesIntoLatex(
      feuille.promo,
      feuille.matiere,
      feuille.titre,
      feuille.prof,
      dateFeuille,
      feuille.realDuree,
      "",
      listeContenuExos,
      feuille.dateExamen
    )
    HtmlParser.generatePdf(latexRes)
  }

  //Le formulaire qui devrait servir pour la validation de la page des choix de corriges .
  val corrigeSelecterForm = Form(
    single(
      "group1"->list(text)
    )
  )

  def generationCorrige(id : String)= Action.async {
    implicit  request =>
      Feuille.findById(id).flatMap {
        current =>
          current match {
            case Some(feuille) => {
              Exercice.findAll().map((exercices: List[Exercice]) => exercices.filter((exercice: Exercice) => feuille.listeExos.contains(exercice._id.get))).map {
                listeExos =>
                  Ok(views.html.creationfeuillecorrige(feuille,listeExos))
              }
            }
            case None => {
              Future(Redirect(routes.FeuilleControllerBdd.consultation).flashing("notExisted" -> "La feuille à générer n'existe pas !"))
            }
          }
      }
  }

  //cette fonctiond devra valider le choix des corrigés pour une feuille d'exercices puis envoyer les corrigés choisis à la page de visualisation de corrigés pour génération
  def submitChoixCorriges(id: String) =TODO


  //Cette fonction sera basée sur la même architecture que la visualisation d'une feuille générée à publier
  def visualisationCorrige = TODO


  //A priori la generation de PDF sera la même fonction que la generation d'une feuille, avec juste les corriges en plus
  def generateCorrigePdf(feuille: Feuille, listeCorriges : List[String]) = {
    val dateFeuille = feuille.dateModification.year().get()+"\\}"+"\\{"+feuille.dateModification.monthOfYear().get()+"\\}"+"\\{"+feuille.dateModification.dayOfMonth().get()
    val latexRes = HtmlParser.parseHtmlExercisesIntoLatex(
      feuille.promo,
      feuille.matiere,
      feuille.titre,
      feuille.prof,
      dateFeuille,
      feuille.realDuree,
      "",
      listeCorriges,
      feuille.dateExamen
    )
    HtmlParser.generatePdf(latexRes)
  }


}
