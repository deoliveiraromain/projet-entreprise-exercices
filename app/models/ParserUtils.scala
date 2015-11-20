package models

import scala.util.matching.Regex
import scala.annotation.tailrec

object ParserUtils {

  /**
   * Retourne le contenu des deux balises
   * Inutile pour l'instant
   *
   * @param firstItem   Premier élément de l'encadrement
   * @param lastItem    Dernier élément de l'encadrement
   * @param content     Contenu à parser
   * @return
   */
  def parseBetween(firstItem:String, lastItem:String, content: String):String = {
    content.split(firstItem)(1).split(lastItem)(0)
  }

  /**
   * Retourne ce qu'il y a après l'item de la chaîne de caractères
   * @param item      Element à partir duquel garder le contenu
   * @param content   Contenu à tronquer
   * @return
   */
  def getContentAfter(item:String, content:String):String = {
    content.split(item)(1)
  }

  /**
   * Remplace toutes les occurences du pattern trouvées dans la source
   * @param pattern       Pattern à chercher
   * @param replacement   Remplacement à effectuer
   * @param source        Contenu dans lequel chercher le pattern
   * @return
   */
  def replacePattern(pattern:String, replacement:String, source:String): String = {
    val regexPattern = new Regex(pattern)
    regexPattern replaceAllIn(source, replacement)
  }

  /**
  * Remplace la première les occurences du pattern trouvées dans la source
  * @param pattern       Pattern à chercher
  * @param replacement   Remplacement à effectuer
  * @param source        Contenu dans lequel chercher le pattern
    * @return
  */
  def replaceFirstPattern(pattern:String, replacement:String, source:String): String = {
    val regexPattern = new Regex(pattern)
    regexPattern.replaceFirstIn(source, replacement)
  }


  /**
   * Retourne la première occurence du pattern trouvé dans la source
   * @param pattern     Pattern à chercher
   * @param source      Contenu dans lequel chercher le pattern
   * @return
   */
  def findFirstPattern(pattern:String, source:String): Option[String] = {
    val regexPattern = new Regex(pattern)
    regexPattern findFirstIn source
  }



  /**
   * Remplace le contenu mathématique de l'entrée par les balises environnement.
   * Cette Fonction est appelée pour la génération du latex à partir du html
   * @param content   Contenu à traiter
   * @param listRes   Contenu mathématique
   * @param acc       Indice pour le contenu mathématique
   * @return
   */
  @tailrec
  def replaceMathWithTags(content : String, listRes : List[String], acc : Int) : String = {
    listRes match {
      case Nil => content
      case head::tail => replaceMathWithTags(content.replace(head,"//environementMath"+acc+"//"), tail, acc+1)
    }
  }


  /**
   * Remplace le contenu mathématique de l'entrée par les balises environnement.
   * Cette Fonction est appelée pour l'upload d'un fichier latex
   * @param content   Contenu à traiter
   * @param listRes   Contenu mathématique
   * @return
   */
  @tailrec
  def replaceMath(content : String, listRes : List[String]) : String = {
    listRes match {
      case Nil => content
      case head::tail => replaceMath(content.replace(head,"//environementMath//"), tail)
    }
  }



  /**
   * Isole les environnements mathématiques du contenu latex uploadé
   * @param content   Contenu latex
   * @return          Le contenu latex marqué des emplacements des environnements en premier élément
   *                  Une liste des environnements mathématiqueser en second élément
   *
   */
  def isolateMathEnv(content: String): (List[String],List[String]) = {

    var booleanDollar = false
    var booleanDoubleDollar = false
    var booleanFin = false
    var strBuilder = new StringBuilder()
    var listRes = List[String]()
    var i=0
    while(i < content.length) {

      if((booleanDollar || booleanDoubleDollar) && content.charAt(i) == '$') {
        // présence d'une double dollar
        if(booleanDoubleDollar && i+1 != content.length && content.charAt(i+1) == '$') {
          booleanDoubleDollar = false
          i+=1
          strBuilder.append("$")
        } else {
          booleanDollar = false
        }
        booleanFin= true
        strBuilder.append("$")
        listRes = listRes :+ strBuilder.toString()
        strBuilder = new StringBuilder()
      }
      if(!booleanFin) {

        if( content.charAt(i) == '$') {
          // présence d'une double dollar
          if(i+1 != content.length && content.charAt(i+1) == '$') {
            booleanDoubleDollar = true
            i+=1
            strBuilder.append("$")
          } else {
            booleanDollar = true
          }
        }

        if(booleanDoubleDollar || booleanDollar) {
          strBuilder.append(content.charAt(i))
        }
      }  else {
        booleanFin= false
      }
      i+=1
    }

    (replaceMath(content,listRes).split("\n").toList,listRes)
  }


  /**
   * Isole le contenu mathématique d'un contenu html et le remplace par des balises numérotées
   * La seconde liste contient le code mathématique en latex
   * @param content Contenu Html
   * @return
   */
  def isolateMathEnvFromHtml(content: String): (List[String],List[String]) = {

    var listHtmlToReplace = List[String]()
    var listMath = List[String]()
    val arraySpan = content.split("<span class=\"math-tex\" data-latex=\"")
    arraySpan.foreach(el => {
      val contentMath = el.split("</span>")
      val contentUseful = contentMath(0).split("\">")(0)
      val betweenSpan = "<span class=\"math-tex\" data-latex=\""+contentMath(0) + "</span>"
      listHtmlToReplace = betweenSpan +: listHtmlToReplace
      listMath = contentUseful +: listMath
    })

    val newContent = replaceMathWithTags(content, listHtmlToReplace,0)
    (newContent.split("\n").toList,listMath)
  }


  /**
   * Remplace les environnements maths numérotés par le contenu mathématique latex correspondant.
   * Cette fonction est utilisée pour la génération du latex à partir de l'html
   * @param content   Contenu à traiter
   * @param listRes   Liste des contenus mathématiques
   * @param i         Indice de l'environnement mathématique
   * @return
   */
  @tailrec
  def replaceMathForLatex(content : String, listRes : List[String], i : Int) : String = {
    listRes match {
      case Nil =>
        content
      case head::tail =>
        replaceMathForLatex(content.replace("//environementMath"+i+"//",head), tail, i+1)
    }
  }


  /**
   * Replace les environnements mathématiques dans le contenu html généré
   * @param content     Contenu hmtl balisé avec les emplacements mathématiques
   * @param env         Contenu des environnements mathématiques
   * @return            Contenu latex comprenant les environnements mathématiques
   */
  def insertMathEnv(content:List[String], env: List[String]):List[String] = {

    @tailrec
    def insertMathRec(content:List[String], env:List[String], res:List[String]): List[String] = {
      if(content == Nil){
        res.reverse
      }else{
        val pattern = "//environementMath//"
        if(findFirstPattern(pattern,content.head).nonEmpty){
          val newLine = content.head.subSequence(0, content.head.indexOf(pattern)).toString+"<span class=\"math-tex\" data-latex=\""+env.head+"\">\\( "+env.head.filter(_ != '$')+" \\)</span>"+content.head.subSequence(content.head.indexOf(pattern)+pattern.size,content.head.size).toString
          insertMathRec(newLine::content.tail, env.tail, res)
        }else{
          insertMathRec(content.tail, env, content.head::res)
        }
      }
    }
    insertMathRec(content,env, Nil)
  }

  /**
   * Remplace les caractères spéciaux dûs au html en leur équivalent latex
   * @param content   Contenu Html
   * @return
   */
  def replaceSpecialChars(content: List[String]):List[String] = {
    replacePattern("&#39;", "'",
      replacePattern("&nbsp;", " ",
        replacePattern("\\\\nolinebreak", "",
          replacePattern("<span> </span>", " ",
            replacePattern("&lt;","<",
              replacePattern("&gt;",">",content.mkString("\n"))))))).split("\n").toList
  }
}
