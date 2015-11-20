package models

import reactivemongo.api.Cursor
import play.modules.reactivemongo.ReactiveMongoPlugin
import play.modules.reactivemongo.json.collection.JSONCollection
import play.api.libs.concurrent.Execution.Implicits._
import reactivemongo.bson._
import play.api.libs.json._
import play.api.Play.current
import scala.concurrent.Future
import play.modules.reactivemongo.json.BSONFormats._
import org.joda.time.{Duration, Period, DateTime}
import scala.util.{Success, Failure}
import reactivemongo.core.commands.Count

case class Feuille (

                     _id: Option[BSONObjectID],
                     prof :  Option[String],
                     titre : String,
                    module :String,
                     matiere: String,
                     promo : String,
                     option : Option[String],
                     typefeuille : String,
                     duree : Option[String],
                    dateCreation: DateTime,
                    dateModification: DateTime,
                    realDuree: Option[String],
                    dateExamen : Option[String],
                     listeExos : List[BSONObjectID] )

object Feuille  {

  implicit val format = Json.format[Feuille]

  def collection: JSONCollection = ReactiveMongoPlugin.db.collection("feuilleExos")

  def insert(feuille : Feuille) = {
    collection.insert(feuille)
    feuille.listeExos.foreach(id =>Exercice.updateLastUsedDate(id,feuille.dateCreation))
  }



  def findByTitle(titre: String) : Future[Option[Feuille]] = {
    collection.find(Json.obj("titre" -> titre)).one[Feuille]
  }


  def findAll() : Future[List[Feuille]]  = {

    val cursor: Cursor[Feuille] = collection.find(Json.obj()).sort(Json.obj("titre" -> 1)).cursor[Feuille]
    val futureUsersList : Future[List[Feuille]]   = cursor.collect[List]()
    futureUsersList
  }

  def findById(id: String): Future[Option[Feuille]] = {
    collection.find(Json.obj("_id" -> BSONObjectID(id))).one[Feuille]
  }




  def delete(id: String): Unit = {
    collection.remove(Json.obj("_id" -> BSONObjectID(id))).onComplete {
      case Failure(e) => throw e
      case Success(_) => Console.print("successful!")
    }
  }

  def update(id: String, newFeuille: Feuille): Unit = {

    val modifier = Json.obj(
      "$set" -> Json.obj(
        "prof" -> newFeuille.prof,
        "titre" -> newFeuille.titre,
        "module" -> newFeuille.module,
        "matiere" -> newFeuille.matiere,
        "promo" -> newFeuille.promo,
        "option" -> newFeuille.option,
        "typefeuille" -> newFeuille.typefeuille,
        "duree" -> newFeuille.duree,
        "dateModification" -> new DateTime(),
        "listeExos" -> newFeuille.listeExos
      ))

    collection.update(Json.obj("_id" -> BSONObjectID(id)), modifier)
  }

  //Fonction qui renvoie une Future liste qui contient les n derniers feuilles
  def lastFeuilles(n : Int)  = {
    val cursor: Cursor[Feuille] = collection.find(Json.obj()).sort(Json.obj("dateCreation" -> -1)).cursor[Feuille]
    val futureExercicesList : Future[List[Feuille]]   = cursor.collect[List](n)
    futureExercicesList
  }


  //Là il faudra faire un .map pour obtenir un int contenant le nb total de feuilles
  def nombreFeuillesTotal() = {
    val res: Count = Count(collection.name)
    ReactiveMongoPlugin.db.command(res)
  }

  //renvoit la feuille qui a utilisé un certain exercice pour la dernière fois., on fait un .map dessus pour avoir la feuille et à partir de là on
  //peut avoir la date
  def lastUseExercice(exo : Exercice): Future[Option[Feuille]] ={
   collection.find(Json.obj("listeExos"->Some(exo._id))).sort(Json.obj("dateCreation" -> -1)).one[Feuille]
  }


  def updateExoFeuille(id: String,ancienneduree: String, newduree: String)= {
    val cursor: Cursor[Feuille] = collection.find(Json.obj("listeExos"->BSONObjectID(id))).cursor[Feuille]
    val futureFeuilleList =cursor.collect[List]()

    val durationHeure = 1000*60*60 // millisecondes dans une heure
    val durationMin = 1000*60 // millisecondes dans une minute

    var spliten = ancienneduree split "h"
    var spliten2 = spliten(1) split "min"

    var longHours = spliten(0).replaceAll(" ","").toLong * durationHeure
    var longMin = spliten2(0).replaceAll(" ","").toLong * durationMin
    val totalDurationExoAncienne = longHours + longMin


     spliten = newduree split "h"
     spliten2 = spliten(1) split "min"

     longHours = spliten(0).replaceAll(" ","").toLong * durationHeure
     longMin = spliten2(0).replaceAll(" ","").toLong * durationMin
     val totalDurationExoNew = longHours + longMin



    futureFeuilleList.map(listeFeuille => listeFeuille.foreach
      (feuille=> {

        val spliten = feuille.duree.get split "h"
        val spliten2 = spliten(1) split "min"

        val longHours = spliten(0).replaceAll(" ","").toLong * durationHeure
        val longMin = spliten2(0).replaceAll(" ","").toLong * durationMin
        val totalDurationFeuille = longHours + longMin

        val newDurationFeuille = totalDurationFeuille - totalDurationExoAncienne  + totalDurationExoNew
        val period : Period = new Period(newDurationFeuille)
        val modifDureeBdd = period.getHours + " h " + period.getMinutes + " min"


        val modifier = Json.obj(
          "$set" -> Json.obj(
            "duree" ->modifDureeBdd
          ))

        collection.update(Json.obj("_id" -> feuille._id), modifier)
      } ))
  }


  def delExoFeuille(id : String, duree : String)={

    val cursor: Cursor[Feuille] = collection.find(Json.obj("listeExos"->BSONObjectID(id))).cursor[Feuille]
    val futureFeuilleList =cursor.collect[List]()

    val durationHeure = 1000*60*60 // millisecondes dans une heure
    val durationMin = 1000*60 // millisecondes dans une minute

    val spliten = duree split "h"
    val spliten2 = spliten(1) split "min"

    val longHours = spliten(0).replaceAll(" ","").toLong * durationHeure
    val longMin = spliten2(0).replaceAll(" ","").toLong * durationMin
    val totalDurationExo = longHours + longMin



    futureFeuilleList.map(listeFeuille => listeFeuille.foreach
      (feuille=> {

        val spliten = feuille.duree.get split "h"
        val spliten2 = spliten(1) split "min"

        val longHours = spliten(0).replaceAll(" ","").toLong * durationHeure
        val longMin = spliten2(0).replaceAll(" ","").toLong * durationMin
        val totalDurationFeuille = longHours + longMin

        val newDurationFeuille = totalDurationFeuille - totalDurationExo
        val period : Period = new Period(newDurationFeuille)
        val modifDureeBdd = period.getHours + " h " + period.getMinutes + " min"

          if (modifDureeBdd != "0 h 0 min"){
            val modifier = Json.obj(
              "$set" -> Json.obj(
                "duree" ->modifDureeBdd
              ))

            val modifier2 = Json.obj(
              "$pull" -> Json.obj(
                "listeExos" -> BSONObjectID(id))
            )

            collection.update(Json.obj("_id" -> feuille._id), modifier)
            collection.update(Json.obj("_id" -> feuille._id), modifier2)
          }
        else {
            collection.remove(Json.obj("_id" -> feuille._id))
          }
      } ))
  }

}
