package models

import reactivemongo.bson._
import reactivemongo.api.Cursor
import play.modules.reactivemongo.ReactiveMongoPlugin
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json._
import play.api.Play.current
import scala.concurrent.Future
import play.modules.reactivemongo.json.BSONFormats._
import org.joda.time.DateTime
import reactivemongo.core.commands.Count
import scala.util.Success
import play.modules.reactivemongo.json.collection.JSONCollection
import scala.util.Failure
import scala.Some
import scala.None

case class Exercice(

                     _id: Option[BSONObjectID],
                     titre: String,
                     module: String,
                     matiere: String,
                     chapitre: Option[String],
                     motsCles: Option[String],
                     typeExercice: String,
                     duree: Option[String],
                     dateCreation: DateTime,
                     dateModification: DateTime,
                     contenu: String,
                     corrige: Option[List[String]],
                     lastUsed: Option[DateTime]
                     )

object Exercice {

  implicit val format = Json.format[Exercice]

  def collection: JSONCollection = ReactiveMongoPlugin.db.collection("exercices")


  def insert(exercice: Exercice) {
    collection.insert(exercice)
  }


  def delete(id: String): Future[Unit] = {

    collection.find(Json.obj("_id" -> BSONObjectID(id))).one[Exercice].flatMap {
      exo =>
        collection.remove(Json.obj("_id" -> BSONObjectID(id))).flatMap {
          _ =>
            Feuille.delExoFeuille(id, exo.get.duree.get)
        }
    }
  }


  def findById(id: BSONObjectID): Future[Option[Exercice]] = {
    collection.find(Json.obj("_id" -> id)).one[Exercice]
  }

  def findById(id: String): Future[Option[Exercice]] = {
    Console.print("id : " + id)
    collection.find(Json.obj("_id" -> BSONObjectID(id))).one[Exercice]
  }


  def findAll(): Future[List[Exercice]] = {

    val cursor: Cursor[Exercice] = collection.find(Json.obj()).sort(Json.obj("dateCreation" -> -1)).cursor[Exercice]
    val futureExercicesList: Future[List[Exercice]] = cursor.collect[List]()
    futureExercicesList
  }


  def findNewest(): Future[Option[Exercice]] = {
    collection.find(Json.obj()).sort(Json.obj("_id" -> -1)).one[Exercice]
  }

  def removeCorrige(idExercise: String, pos: Int): Future[Future[String]] = {

    val unset = Json.obj(
      "$unset" -> Json.obj(
        "corrige." + pos -> 1
      ))
    val pull = Json.obj(
      "$pull" -> Json.obj(
        "corrige" -> BSONNull
      ))

    collection.update(Json.obj("_id" -> BSONObjectID(idExercise)), unset).map {
      _ =>
        val res: Future[String] = collection.update(Json.obj("_id" -> BSONObjectID(idExercise)), pull).map {
          _ =>
            idExercise
        }
        res
    }
  }

  def update(id: String, newExercice: Exercice): Future[Exercice] = {

    def updateExercice(js: JsObject): Future[Exercice] = {
      collection.find(Json.obj("_id" -> BSONObjectID(id))).one[Exercice].flatMap {
        exo: Option[Exercice] =>
          exo match {
            case Some(exercice) =>
              collection.update(Json.obj("_id" -> BSONObjectID(id)), js).map {
                _ =>
                  if (exercice.duree.get != newExercice.duree.get) {
                    Feuille.updateExoFeuille(id, exercice.duree.get, newExercice.duree.get)
                  }
                  newExercice
              }
            case None => throw new Exception
          }
      }
    }
    newExercice.corrige match {
      case Some(corrige) =>
        val modifier: JsObject = Json.obj(
          "$set" -> Json.obj(
            "titre" -> newExercice.titre,
            "module" -> newExercice.module,
            "matiere" -> newExercice.matiere,
            "chapitre" -> newExercice.chapitre,
            "motsCles" -> newExercice.motsCles,
            "typeExercice" -> newExercice.typeExercice,
            "duree" -> newExercice.duree,
            "dateModification" -> new DateTime(),
            "contenu" -> newExercice.contenu,
            "corrige" -> newExercice.corrige
          ))
        updateExercice(modifier)
      case None =>
        val modifierWithoutCorrige: JsObject =
          Json.obj("$set" -> Json.obj(
            "titre" -> newExercice.titre,
            "module" -> newExercice.module,
            "matiere" -> newExercice.matiere,
            "chapitre" -> newExercice.chapitre,
            "motsCles" -> newExercice.motsCles,
            "typeExercice" -> newExercice.typeExercice,
            "duree" -> newExercice.duree,
            "dateModification" -> new DateTime(),
            "contenu" -> newExercice.contenu
          ))
        updateExercice(modifierWithoutCorrige)
    }
  }

  def addCorrection(id: String, newCorrige: String, position: Int): Unit = {
    if (position != -1) {
      val modifier = Json.obj(
        "$set" -> Json.obj(
          "corrige." + position -> newCorrige
        ))
      collection.update(Json.obj("_id" -> BSONObjectID(id)), modifier).onComplete {
        case Failure(e) => throw e
        case Success(_) => Console.print("successful!")
      }
    } else {
      val modifier = Json.obj(
        "$push" -> Json.obj(
          "corrige" -> newCorrige
        ))
      val pull = Json.obj(
        "$pull" -> Json.obj(
          "corrige" -> BSONNull
        ))
      collection.update(Json.obj("_id" -> BSONObjectID(id)), pull).onComplete {
        case Failure(e) => throw e
        case Success(_) => collection.update(Json.obj("_id" -> BSONObjectID(id)), modifier).onComplete {
          case Failure(e) => throw e
          case Success(_) => Console.print("successful!")
        }
      }
    }
  }

  def updateLastUsedDate(id: BSONObjectID, lastUsed: DateTime) = {

    val modifier = Json.obj(
      "$set" -> Json.obj(
        "lastUsed" -> lastUsed
      ))

    collection.update(Json.obj("_id" -> id), modifier).onComplete {
      case Failure(e) => throw e
      case Success(_) => Console.print("successful!")
    }
  }


  //Fonction qui renvoie une Future liste qui contient les exercices possédant un corrigé
  def exosCorrigés() = {
    val cursor: Cursor[Exercice] = collection.find(Json.obj("corrige" -> Json.obj("$exists" -> true))).cursor[Exercice]
    val futureExercicesList: Future[List[Exercice]] = cursor.collect[List]()
    futureExercicesList
  }

  //Renvoie après un .map le nombre d'exos corrigés.
  def nombreExosCorrigés() = {
    val res: Count = Count(collection.name, Some(BSONDocument("corrige" -> BSONDocument("$exists" -> true))))
    ReactiveMongoPlugin.db.command(res)
  }


  //Fonction qui renvoie une Future liste qui contient les N dernires exercices possédant un corrigé
  def lastexosCorrigés(n: Int): Future[List[Exercice]] = {
    val cursor: Cursor[Exercice] = collection.find(Json.obj("corrige" -> Json.obj("$exists" -> true))).sort(Json.obj("dateModification" -> -1)).cursor[Exercice]
    val futureExercicesList: Future[List[Exercice]] = cursor.collect[List](n)
    futureExercicesList
  }

  //Fonction qui renvoie une Future liste qui contient les n derniers exos
  def lastExos(n: Int) = {
    val cursor: Cursor[Exercice] = collection.find(Json.obj()).sort(Json.obj("dateCreation" -> -1)).cursor[Exercice]
    val futureExercicesList: Future[List[Exercice]] = cursor.collect[List](n)
    futureExercicesList
  }

  //Là il faudra faire un .map pour obtenir un int contenant le nb total d'exo
  def nombreExosTotal() = {
    val res: Count = Count(collection.name)
    ReactiveMongoPlugin.db.command(res)
  }
}