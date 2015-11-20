package controllers

import play.api.mvc._
import models.{Feuille, Exercice}
import play.api.libs.concurrent.Execution.Implicits.defaultContext

object Application extends Controller {

  def pageNotFound() = play.mvc.Results.TODO

  def homePage = Action {
    Ok(views.html.homepage())
  }

  def synthese = Action.async{
    for{
      nbExos <- Exercice.nombreExosTotal()
      nbFeuilles <- Feuille.nombreFeuillesTotal()
      exosCorriges <- Exercice.nombreExosCorrigés()
      cinqDerniersExos <- Exercice.lastExos(5)
      cinqDernieresFeuilles <- Feuille.lastFeuilles(5)
      cinqDerniersExosCorriges <- Exercice.lastexosCorrigés(5)
    } yield{
      val pourcentageCorriges = "%.2f".format((exosCorriges.toFloat/nbExos)*100)
      Ok(views.html.synthese(cinqDerniersExos, cinqDernieresFeuilles, cinqDerniersExosCorriges, nbExos, nbFeuilles,exosCorriges,pourcentageCorriges))
    }
  }
}