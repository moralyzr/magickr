enablePlugins(JavaAppPackaging)

name := "magickr"
organization := "com.moralyzr"
version := "0.0.1"
scalaVersion := "3.0.1"

scalacOptions := Seq("-unchecked", "-deprecation")

libraryDependencies ++= {
  val akkaVersion = "2.6.15"
  val akkaHttpVersion = "10.2.4"
  val akkaHttpCirceVersion = "1.37.0"
  val catsVersion = "2.6.1"
  val catsEffectVersion = "3.2.2"
  val catsEffectTestVersion = "1.2.0"
  val catsEffectTestkitVersion = "3.2.2"
  val catsMtlVersion = "1.2.1"
  val circeVersion = "0.14.1"
  val doobieVersion = "1.0.0-M5"
  val flywayVersion = "7.12.1"
  val jwtCirceVersion = "9.0.0"
  val logbackVersion = "1.2.5"
  val scalaBcryptVersion = "4.3.0"
  val scalaTestVersion = "3.2.9"
  val scalaLoggingVersion = "3.9.4"
  val sconfigVersion = "1.4.4"

  Seq(
    "org.typelevel" %% "cats-core" % catsVersion,
    "org.typelevel" %% "cats-kernel" % catsVersion,
    "org.typelevel" %% "cats-effect" % catsEffectVersion,
    "org.typelevel" %% "cats-mtl" % catsMtlVersion,
    "io.circe" %% "circe-core" % circeVersion,
    "io.circe" %% "circe-parser" % circeVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "org.tpolecat" %% "doobie-core" % doobieVersion,
    "org.tpolecat" %% "doobie-hikari" % doobieVersion,
    "org.tpolecat" %% "doobie-postgres" % doobieVersion,
    "org.flywaydb" % "flyway-core" % flywayVersion,
    "com.github.jwt-scala" %% "jwt-circe" % jwtCirceVersion,
    "ch.qos.logback" % "logback-classic" % logbackVersion,
    "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion,
    "org.ekrich" %% "sconfig" % sconfigVersion,
    "org.scalactic" %% "scalactic" % scalaTestVersion,
    "org.scalatest" %% "scalatest" % scalaTestVersion % "test",
    "org.typelevel" %% "cats-effect-testing-scalatest" % catsEffectTestVersion % "test",
    "org.typelevel" %% "cats-effect-testkit" % catsEffectTestkitVersion % "test",
    "org.tpolecat" %% "doobie-h2" % doobieVersion % "test"
  ) ++ Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "de.heikoseeberger" %% "akka-http-circe" % akkaHttpCirceVersion,
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
    "com.github.t3hnar" %% "scala-bcrypt" % scalaBcryptVersion,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % "test"
  ).map(_.cross(CrossVersion.for3Use2_13))
}

//Compile / guardrailTasks := List(
//  ScalaServer(file("src/main/resources/api.yaml"), pkg="com.moralyzr.magickr.adapters.gen", tracing=true)
//)

Revolver.settings
