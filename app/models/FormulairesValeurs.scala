package models



object FormulairesValeurs {

  val listpromo = List(
    "CPI1",
    "CPI2",
    "ING1",
    "ING2-GSI",
    "ING2-SIE",
    "ING2-MI",
    "ING2-IFI",
    "ING3"
  )

  val listoptions = List(
    "",
    "ICC",
    "IERP",
    "I3",
    "ICOM",
    "GL",
    "IFI",
    "IAD",
    "BI"
  )

  val listtypes= List(
    "Exercices de cours",
    "Travaux dirigés",
    "Examen"
  )

  val listdureesfeuille= List(
      "0 h 00 min ",
      "0 h 30 min",
      "1 h 0 min",
      "1 h 30 min",
      "2 h 00 min",
      "2 h 30 min",
      "3 h 00 min",
      "3 h 30 min",
      "4 h 00 min"
  )

  val listdureesexercice= List(
    "0 h 00 min",
    "0 h 10 min",
    "0 h 15 min",
    "0 h 20 min",
    "0 h 30 min",
    "0 h 40 min",
    "0 h 50 min",
    "1 h 00 min",
    "1 h 15 min",
    "1 h 30 min",
    "2 h 00 min",
    "2 h 30 min",
    "3 h 00 min"
  )

  val exercicestypes= List(
    "Exercice",
    "Question de cours",
    "Problème"
  )
}