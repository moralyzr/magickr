enablePlugins(JavaAppPackaging)

name := "magickr"
organization := "com.moralyzr"
version := "0.0.1"
scalaVersion := "3.1.0"

scalacOptions := Seq("-unchecked", "-deprecation")

libraryDependencies ++= {
  val catsVersion              = "2.6.1"
  val catsEffectVersion        = "3.2.8"
  val catsEffectTestVersion    = "1.3.0"
  val catsEffectTestkitVersion = "3.2.8"
  val catsMtlVersion           = "1.2.1"
  val circeVersion             = "0.14.1"
  val doobieVersion            = "1.0.0-M5"
  val flywayVersion            = "8.0.0"
  val http4sVersion            = "1.0.0-M29"
  val jwtCirceVersion          = "9.0.2"
  val logbackVersion           = "1.2.6"
  val scalaBcryptVersion       = "4.3.0"
  val scalaTestVersion         = "3.2.9"
  val scalaLoggingVersion      = "3.9.4"
  val sconfigVersion           = "1.4.5"

  Seq(
    "org.typelevel" %% "cats-core" % catsVersion,
    "org.typelevel" %% "cats-kernel" % catsVersion,
    "org.typelevel" %% "cats-effect" % catsEffectVersion,
    "org.typelevel" %% "cats-mtl" % catsMtlVersion,
    "io.circe" %% "circe-core" % circeVersion,
    "io.circe" %% "circe-parser" % circeVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "com.typesafe" % "config" % "1.4.1",
    "org.tpolecat" %% "doobie-core" % doobieVersion,
    "org.tpolecat" %% "doobie-hikari" % doobieVersion,
    "org.tpolecat" %% "doobie-postgres" % doobieVersion,
    "org.flywaydb" % "flyway-core" % flywayVersion,
    "org.http4s" %% "http4s-blaze-server" % http4sVersion,
    "org.http4s" %% "http4s-circe" % http4sVersion,
    "org.http4s" %% "http4s-dsl" % http4sVersion,
    "com.github.jwt-scala" %% "jwt-circe" % jwtCirceVersion,
    "ch.qos.logback" % "logback-classic" % logbackVersion,
    "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion,
    "org.ekrich" %% "sconfig" % sconfigVersion,
    "org.scalactic" %% "scalactic" % scalaTestVersion,
    "org.scalatest" %% "scalatest" % scalaTestVersion % Test,
    "org.typelevel" %% "cats-effect-testing-scalatest" % catsEffectTestVersion % Test,
    "org.typelevel" %% "cats-effect-testkit" % catsEffectTestkitVersion % Test,
    "org.scalacheck" %% "scalacheck" % "1.15.4" % Test,
    "org.tpolecat" %% "doobie-h2" % doobieVersion % Test
  ) ++ Seq(
    "com.github.t3hnar" %% "scala-bcrypt" % scalaBcryptVersion,
    "io.github.etspaceman" %% "scalacheck-faker" % "7.0.0" % Test
  ).map(_.cross(CrossVersion.for3Use2_13))
}

//Compile / guardrailTasks := List(
//  ScalaServer(file("src/main/resources/api.yaml"), pkg="com.moralyzr.magickr.adapters.gen", tracing=true)
//)

Revolver.settings
