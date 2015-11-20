package models

import sys.process._
import java.io._
import scala.io.Source
import scala.annotation.tailrec
import ParserUtils._
import java.io.File


object LatexParser {
  /**
   * Remplace les balises TextbookQuestions par les balises latex section personnalisées
   * @param content   Contenu dans lequel a lieu le remplacement potentiel d'une question de cours
   * @return
   */
  def replaceIntoLatexTempTextbookQuestions(content:String): String = {
    replacePattern("begin\\{TextbookQuestions\\}","section*{cours/#/Questions de cours}",content)
    replacePattern("end\\{TextbookQuestions\\}","section*{fin-cours/#/}",content)
  }


  /**
   * Remplace les balises Exercise par les balises latex section personnalisées
   * @param content   Contenu dans lequel a lieu le remplacement potentiel d'un exercice
   * @return
   */
  def replaceIntoLatexTempExercise(content:String): String = {
    replacePattern("begin\\{Exercise\\}","section*{exo/#/Exercice}",
      replacePattern("end\\{Exercise\\}","section*{fin-exo/#/}",content)
    )
  }

  /**
   * Remplace les balises Problem par les balises latex section personnalisées
   * @param content   Contenu dans lequel a lieu le remplacement potentiel d'un problème
   * @return
   */
  def replaceIntoLatexTempProblem(content:String): String = {
    replacePattern("begin\\{Problem\\}","section*{pb/#/Probleme}",
      replacePattern("end\\{Problem\\}","section*{fin-pb/#/}",content)
    )
  }


  /**
   * Remplace les balises questions par les balises latex subsection personnalisées
   * @param content   Contenu dans lequel a lieu le remplacement potentiel des questions
   * @return
   */
  def replaceIntoLatexTempQuestions(content:String): String = {
    replacePattern("begin\\{questions\\}","subsection*{quest/#/}",
      replacePattern("end\\{questions\\}","subsection*{fin-quest/#/}",content)
    )
  }


 /**
   * Remplace les balises subquestions par les balises latex subsubsection personnalisées
   * @param content ligne à traiter
   * @return        la même ligne potentiellement remplacée
   */
  def replaceIntoLatexTempSubQuestions(content:String): String = {
    replacePattern("begin\\{subquestions\\}","subsubsection*{subquest/#/}",
      replacePattern("end\\{subquestions\\}","subsubsection*{fin-subquest/#/}",content)
    )
  }


  /**
   *  Remplace l'eventuelle balise personelle d'une ligne latex
   * @param content   Ligne du latex à traiter
   * @return
   */
  def replaceLatexLine(content:String): String = {
    replaceIntoLatexTempTextbookQuestions(
      replaceIntoLatexTempExercise(
        replaceIntoLatexTempProblem(
          replaceIntoLatexTempQuestions(
            replaceIntoLatexTempSubQuestions(content)
          )
        )
      )
    )
  }


  /**
   * Remplace toutes les lignes du latex d'origine pour les formaliser en langage latex de base
   *
   * @param listLines la liste de lignes à potentiellement remplacer
   * @return          la liste en entrée dont les balises personnalisées ont été remplacées
   */
  def replaceLatexLines(listLines: List[String]): List[String] = {
    listLines.map(line => replaceLatexLine(line))
  }



  /**
   *  Parcours récursivement la liste et ajoute les balises :
   *    \begin{enumerate} après la balise de début d'une question ou sous question
   *    \end{enumerate} avant la balise de fin d'une question ou sous question
   *
   * @param content   Contenu latex dans lequel a lieu le remplacement potentiel
   */
  def insertItemizeForQuestions(content: List[String]):List[String] = {
    @tailrec
    def insertItemizeForQuestionsRec(content: List[String], result:List[String]): List[String] = {
      if (content == Nil){
        result.reverse
      }
      else{
        if(findFirstPattern("fin-quest/#/|fin-subquest/#/",content.head).nonEmpty){
          insertItemizeForQuestionsRec(content.tail,"""\end{enumerate}"""::result)
        }
        else if(findFirstPattern("subquest/#/",content.head).nonEmpty){
          insertItemizeForQuestionsRec(content.tail,"""\begin{enumerate}"""::result)
        }
        else if(findFirstPattern("quest/#/",content.head).nonEmpty){
          insertItemizeForQuestionsRec(content.tail,"""\begin{enumerate}[exo/#/quest]"""::result)
        }
        else{
          insertItemizeForQuestionsRec(content.tail,content.head::result)
        }
      }
    }
    insertItemizeForQuestionsRec(content,Nil)
  }


  /**
   * Teste si la ligne est le début d'un exercice
   * @param line    Ligne html à traiter
   * @return        Vrai si la ligne est le début d'un exercice, Faux sinon
   */
  def isExercice(line:String):Boolean = {
    findFirstPattern("<h2>exo/#/Exercice</h2>",line).nonEmpty
  }

  /**
   * Teste si la ligne est le début d'une question de cours
   * @param line    Ligne html à traiter
   * @return        Vrai si la ligne est le début d'une question de cours, Faux sinon
   */
  def isCours(line:String):Boolean = {
    findFirstPattern("<h2>cours/#/Questions de cours</h2>",line).nonEmpty
  }

  /**
   * Teste si la ligne est le début d'un problème
   * @param line    Ligne html à traiter
   * @return        Vrai si la ligne est le début d'un problème, Faux sinon
   */
  def isProblem(line:String):Boolean = {
    findFirstPattern("<h2>pb/#/Probleme</h2>",line).nonEmpty
  }


  /**
   * Enlève la notation de l'exercice si elle a été entrée dans le latex d'origine
   * @param content Contenu html d'un exercice à traiter
   * @return
   */
  def filterNotationFromExercise(content: List[String]):List[String] = {
    if(findFirstPattern("""[\d]""",content.head).nonEmpty){
      content.tail
    }else{
      content
    }
  }

  /**
   * Change la classe des balises ul adéquates pour identifier les questions dans ckEditor
   * @param content contenu d'un exercice
   * @return
   */
  def setQuestionsClassFromHtml(content:List[String]):List[String] = {
    content.map{line=>
      if(findFirstPattern("exo/#/quest",line).nonEmpty){
        """<ol class="exo-question-format">"""
      }else{
        line
      }
    }
  }



  /**
   * Extrait d'un contenu HTML le premier type d'exercice trouvé
   *
   * @param contentType   Type de l'exercice
   * @param content       Contenu de l'exercice
   * @return              Contenu du premier exercice de type demandé
   */
  def getFirstExerciseContentFromHtml(contentType: String, content: List[String]): List[String] = {
    val newContent = setQuestionsClassFromHtml(content)
    contentType match{
      case "exercice" => filterNotationFromExercise(parseBetween("<h2>exo/#/Exercice</h2>","<h2>fin-exo/#/</h2>",newContent.mkString("/###/")).split("/###/").toList)
      case "cours" => filterNotationFromExercise(parseBetween("<h2>cours/#/Questions de cours</h2>","<h2>fin-cours/#/</h2>",newContent.mkString("/###/")).split("/###/").toList)
      case "probleme" => filterNotationFromExercise(parseBetween("<h2>pb/#/Probleme</h2>","<h2>fin-pb/#/</h2>",newContent.mkString("/###/")).split("/###/").toList)
      case _ => throw new Exception("unknown exercise type: possible choices are exercice, cours, or problème")
    }
  }



  /**
   * Retourne une map dans laquelle chaque type d'exercice est lié à son contenu
   * @param content   Contenu html de la feuille d'exercices uploadée
   * @return          Une map qui lie chaque contenu d'exercice (clé) à son type(valeur)
   */
  def getAllExercisesContentFromHtml(content:List[String]):Map[String,String] = {
    /**
     * Fonction récursive qui construit la Map résultat
     * @param content   Contenu html de la feuille d'exercices uploadée
     * @param res       Une map qui lie chaque contenu d'exercice (clé) à son type(valeur)
     * @return          Retourne la Map res
     */
    @tailrec
    def getAllExercisesContentFromHtmlRec(content:List[String], res:Map[String, String]):Map[String,String] = {
      if (content == Nil){
        res
      }
      else{
        if(isExercice(content.head))
          getAllExercisesContentFromHtmlRec(content.tail,res + (getFirstExerciseContentFromHtml("exercice",content).mkString("\n") -> "Exercice"))
        else if(isCours(content.head))
          getAllExercisesContentFromHtmlRec(content.tail, res + (getFirstExerciseContentFromHtml("cours",content).mkString("\n") -> "Cours"))
        else if(isProblem(content.head))
          getAllExercisesContentFromHtmlRec(content.tail, res + (getFirstExerciseContentFromHtml("probleme",content).mkString("\n") -> "Probleme"))
        else
          getAllExercisesContentFromHtmlRec(content.tail, res)
      }
    }

    getAllExercisesContentFromHtmlRec(content,Map())
  }

  /**
   * Enlève les div inutiles mises par TTH dans le Html
   * @param content   Contenu du html à traiter
   * @return
   */
  def removeUnusedDivsFromHtml(content:List[String]): List[String] = {

    @tailrec
    def removeUnusedDivsRec(content:List[String], res:List[String]):List[String] = {
      if(content == Nil){
        res.reverse
      }else{
        if(findFirstPattern("<div>&nbsp;</div>",content.head).nonEmpty){
          removeUnusedDivsRec(content.tail,res)
        }
        else if(findFirstPattern("<div class=\"p\"><!----></div>",content.head).nonEmpty){
          removeUnusedDivsRec(content.tail,res)
        }
        else{
          removeUnusedDivsRec(content.tail, content.head::res)
        }
      }
    }
    removeUnusedDivsRec(content,Nil)
  }



  /**
   * Utilise programme TTH pour transformer le latex normalisé en html
   *
   * @param content   Latex original uploadé par l'utilisateur
   */
  def transformLatexIntoHtml(content:List[String]) = {
    val destTex = "public/tex/texFile.tex"
    val bw = new BufferedWriter(new FileWriter(new File(destTex)))
    val newContent = insertItemizeForQuestions(replaceLatexLines(content))
    newContent.foreach( line => (bw.write(line),bw.newLine())  )
    bw.flush()
    bw.close()
    ("tth " + destTex).!
  }

  /**
   * Analyser le contenu du fichier tex passé en paramètre
   * et retourner une map contenant, pour chaque type d'exercice du latex, le contenu associé.
   *
   * Concernant les environnements mathématiques, ils sont conservés dans l'état d'origine pour garder les templates personnels.
   *
   * @param content   Contenu latex d'origine
   * @return
   */
  def transformAllExercicesIntoHtml(content:List[String]): List[String] = {
    val isolatedContent: (List[String], List[String]) = isolateMathEnv(content.mkString("\n"))
    transformLatexIntoHtml(isolatedContent._1)
    val input = "public/tex/texFile.html"
    val contentSource = Source.fromFile( input,"UTF-8")
    val contentHtml = removeUnusedDivsFromHtml(contentSource.getLines().toList.filter(p=>p!=" "))
    val fullContentHtml = insertMathEnv(contentHtml, isolatedContent._2)
    contentSource.close()
    new File(input).delete()
    new File("public/tex/texFile.tex").delete()
    fullContentHtml
  }

  /**
   * Retourne une map contenant, pour chaque type d'exercice du latex, le contenu associé.
   * @param content   Contenu de tous les exercices en html
   * @return          Une map qui lie chaque contenu d'exercice (clé) à son type(valeur)
   */
  def updateExercisesContentWithHTML(content:List[String]): Map[String, String] = {
    val htmlTransformedContent = transformAllExercicesIntoHtml(content)
    getAllExercisesContentFromHtml(htmlTransformedContent)
  }

  /**
   * Récupère le contenu du premier corrigé trouvé dans le code html passé en paramètre
   * @param content   Contenu html
   * @return
   */
  def getFirstCorrigeContent(content:List[String]):String = {
    val htmlTransformedContent = transformAllExercicesIntoHtml(content)
    val firstCorrige = getFirstExerciseContentFromHtml("exercice",htmlTransformedContent)
    firstCorrige.mkString("\n")
  }


}