package models

import sys.process._
import ParserUtils._
import scala.io.Source
import java.io.{BufferedWriter, FileWriter, File}
import scala.annotation.tailrec


object HtmlParser {
  val headingTemplateLatex:String = "" +
    "\\documentclass[/###promo###/,french]{eisti-exam}/###newline###/" +
    "\\usepackage[scale=0.85]{geometry}/###newline###//###newline###/" +
    "\\usepackage[nothm]{eisti-math}/###newline###//###newline###/" +
    "\\usepackage[french]{datenumber}/###newline###//###newline###/" +
    "\\subject{/###matiere###/}/###newline###/" +
    "\\title{/###titre###/}/###newline###/" +
    "\\author{/###prof###/}/###newline###/" +
    "\\setdatenumber{/###date###/}/###newline###/ " +
    "\\date{\\datedayname{} \\datedate}/###newline###/" +
    "\\duration{/###duree###/}/###newline###/" +
    "\\docs{/###consigneCartouche###/}/###newline###//###newline###/" +
    "\\begin{document}/###newline###//###newline###/" +
    "\\begin{center}/###newline###/" +
    "\\large\\bf Le bar\\^eme est donn\\'e \\`a titre indicatif./###newline###//###newline###/" +
    "\\smallskip/###newline###//###newline###/" +
    "\\large\\bf Il sera tenu compte de la qualit\\'e de la r\\'edactioncet de la pr\\'ecision des justifications./###newline###//###newline###/" +
    "\\medskip/###newline###//###newline###/" +
    "\\normalsize\\sl/###newline###/" +
    " Si vous \\^etes amen\\'e \\`a rep\\'erer ce qui peut vous sembler \\^etre une erreur d'\\'enonc\\'e, vous la signalerez sur votre copie et devrez poursuivre votre composition en expliquant les raisons des initiatives que vous \\^etes amen\\'e \\`a prendre. /###newline###/" +
    "\\end{center}/###newline###//###newline###/" +
    "\\bigskip/###newline###//###newline###/"+
    "\\centerline{$\\diamond$ $\\diamond$}"

  val endingTemplateLatex:String = "\\centerline{$\\diamond$ $\\diamond$}/###newline###/"+
    "\\end{document}"



  /**
   *
   * @param promo               Promotion: cpi1, cpi2, ing1, ing2 ou ing3
   * @param subject             Matière
   * @param title               Titre de la feuille
   * @param prof                Nom et prénom du professeur
   * @param date                date
   * @param duration              durée de la feuille
   * @param instructionsHeading Informations contenues dans le cartouche de la feuille
   * @return                    The modified template in which only exercises content is missing
   */
  def loadSheetInformation(promo:String, subject:String, title:String, prof:String, date:String, duration:String, instructionsHeading:String, dateExamen:Option[String]) = {
    replacePattern("/###promo###/",promo.toLowerCase,
      replacePattern("/###matiere###/",subject,
        replacePattern("/###titre###/",title,
          replacePattern("/###prof###/",prof,
            replacePattern("/###date###/",date,
              replacePattern("/###duree###/", duration,
                replacePattern("/###consigneCartouche###/",instructionsHeading,headingTemplateLatex)))))))
  }

  /**
   * Retourne vrai si la ligne html est le début d'une classe question, faux sinon
   * @param content   Contenu html
   * @return
   */
  def isQuestion(content:String):Boolean = {
    findFirstPattern("class=\"exo-question-format\"", content).nonEmpty
  }


  /**
   * Remplace les balises ol par les balises html correspondantes afin d'être traitées par htmltolatex
   * @param content   Le contenu html initial
   * @return          Le contenu avec les balises adéquates
   */
  def replaceTags(content:List[String]):List[String] = {

    @tailrec
    def replaceTagsRec(content:List[String], res:List[String], treeLevel:Int):List[String] = {
      if (content == Nil){
        res.reverse
      }else{
        if(isQuestion(content.head)){
          replaceTagsRec(content.tail, "<questions>"::res, treeLevel + 1)
        }
        else if(findFirstPattern("<ol>", content.head).nonEmpty && treeLevel > 0){
          // tests pour voir s'il s'agit d'une subquestion ou subsubquestion
          treeLevel match{
            case 1 => replaceTagsRec(content.tail, "<subquestions>"::res, treeLevel + 1)
            case 2 => replaceTagsRec(content.tail, "<subsubquestions>"::res, treeLevel + 1)
            case _ => replaceTagsRec(content.tail, content.head::res, treeLevel + 1)
          }
        }
        else if(findFirstPattern("</ol>", content.head).nonEmpty && treeLevel > 0){
          // tests pour voir quelle balise fermante utiliser
          treeLevel match {
            case 0 => replaceTagsRec(content.tail, "</questions>"::res, treeLevel - 1)
            case 1 => replaceTagsRec(content.tail, "</subquestions>"::res, treeLevel - 1)
            case 2 => replaceTagsRec(content.tail, "</subsubquestions>"::res, treeLevel - 1)
            case _ => replaceTagsRec(content.tail, content.head::res, treeLevel - 1)
          }
        }
        else{
          replaceTagsRec(content.tail, content.head::res, treeLevel)
        }
      }
    }

    replaceTagsRec(content, Nil, 0)
  }


  /**
   * Charge dans le templace existant le contenu des exercices de la feuille
   * @param listExercises   Liste contenant les différents exercices
   * @return
   */
  def includeExercisesContent(listExercises:List[String], templateHeading:String): List[String] = {
    templateHeading.split("/###newline###/").toList ::: listExercises ::: endingTemplateLatex.split("/###newline###/").toList
  }


  /**
   * Englobe le contenu latex des exercices par lse balises \begin{Exercise} et \fin{Exercise}
   * @param latexContent    Liste des contenus HTML pour chaque exercice
   * @return
   */
  def putLatexExercisesTags(latexContent:List[String]):List[String] = {
    latexContent.map(exoContent => "\n<exercise>\n"+exoContent+"\n</exercise>\n")
  }


  /**
   * Transforme le contenu d'un exercice sous forme html en latex formalisé
   * @param content   Contenu de l'exercice en html
   * @return          Contenu en latex
   */
  def transformExerciseHtmlIntoLatex(content:List[String]):String = {
    val contentWithoutNonDesiredElements = replaceTags(content).filter{p=>p.trim.nonEmpty}.mkString("\n").split("\n").toList
    val transformedContent = replacePattern("<br />","",contentWithoutNonDesiredElements.mkString("\n")).split("\n").toList
    val destHtml = "public/htmltolatex/test.html"
    val fichierOut = new File(destHtml)
    val fstream = new FileWriter(fichierOut)
    val bw = new BufferedWriter(fstream)
    val newContent: List[String] = List("<!DOCTYPE html>","<html>","<body>") ::: transformedContent ::: List("</body>","</html>")
    newContent.foreach( line => (bw.write(line),bw.newLine())  )
    bw.flush()
    bw.close()
    ("java -jar public/htmltolatex/htmltolatex.jar -input "+destHtml+" -output test.tex").!
    fichierOut.delete()
    val source = Source.fromFile("test.tex","UTF-8")
    val latexRes = source.getLines().toList.mkString("\n")
    source.close()
    new File("test.tex").delete()
    new File("test.log").delete()
    parseBetween("\\\\begin\\{document\\}", "\\\\end\\{document\\}",latexRes)
  }


  /**
   * Génère le fichier pdf à partir d'un contenu Latex
   * @param content   Contenu du latex
   */
  def generatePdf(content: List[String]) = {
    val dest = "public/fichiersLatexEisti/generatedTex.tex"
    val bw = new BufferedWriter(new FileWriter(new File(dest)))
    content.foreach( line => (bw.write(line),bw.newLine())  )
    bw.flush()
    bw.close()
    ("pdflatex "+dest+" -output-directory=\"public/generation/\"").!
    ("pdflatex "+dest+" -output-directory=\"public/generation/\"").!
  }

  /**
   * Fonction principale qui retourne un document latex formalisé au format List[String] avec l'ensemble des exercices de la feuille
   * Chaque élément de la liste correspond à une ligne du latex
   *
   * @param promo               Promotion: cpi1, cpi2, ing1, ing2 ou ing3
   * @param subject             Matière
   * @param title               Titre de la feuille
   * @param prof                Nom et prénom du professeur
   * @param date                date
   * @param duration            durée de la feuille
   * @param instructionsHeading Informations contenues dans le cartouche de la feuille
   * @param listExercises       Contenu des exercices
   * @return
   */
  def parseHtmlExercisesIntoLatex(promo:String, subject:String, title:String, prof:Option[String],
                                  date:String, duration:Option[String], instructionsHeading:String, listExercises:List[String], dateExamen:Option[String]):List[String] = {
    val fullHtmlContent:List[String] = replaceSpecialChars(putLatexExercisesTags(listExercises).mkString("\n").split("\n").toList)
    val isolatedContentHtml: (List[String], List[String]) = isolateMathEnvFromHtml(fullHtmlContent.mkString("\n"))
    val latexExercisesContent: List[String] = transformExerciseHtmlIntoLatex(isolatedContentHtml._1).split("\n").toList
    val fullContentLatex:List[String] = replaceSpecialChars(replaceMathForLatex(latexExercisesContent.mkString("\n"), isolatedContentHtml._2,0).split("\n").toList)
    val newHeading = loadSheetInformation(promo,subject,title,prof.getOrElse(""),date,duration.getOrElse(""),instructionsHeading, dateExamen)
    includeExercisesContent(fullContentLatex, newHeading)
  }

}