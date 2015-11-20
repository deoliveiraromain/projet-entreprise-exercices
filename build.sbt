name := "projet-entreprise-exercices"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "org.reactivemongo" %% "play2-reactivemongo" % "0.10.2",
  "joda-time" % "joda-time" % "1.5.2"
)


play.Project.playScalaSettings
