enablePlugins(JavaAppPackaging)

name := "magickr"
organization := "com.moralyzr"
version := "0.0.1"
scalaVersion := "3.0.0"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  val akkaVersion = "2.6.15"
  val akkaHttpVersion = "10.2.4"
  val akkaHttpCirceVersion = "1.36.0"
  val catsVersion = "2.6.1"
  val catsEffectVersion = "3.1.1"
  val circeVersion = "0.14.1"
  val doobieVersion = "1.0.0-M5"
  val flywayVersion = "7.11.2"
  val jwtCirceVersion = "8.0.2"
  val logbackVersion = "1.2.3"
  val scalaBcryptVersion = "4.3.0"
  val scalaTestVersion = "3.2.9"
  val scalaLoggingVersion = "3.9.4"
  val sconfigVersion = "1.4.4"

  Seq(
    "org.typelevel" %% "cats-core" % catsVersion,
    "org.typelevel" %% "cats-kernel" % catsVersion,
    "org.typelevel" %% "cats-effect" % catsEffectVersion,
    "io.circe" %% "circe-core" % circeVersion,
    "io.circe" %% "circe-parser" % circeVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "org.tpolecat" %% "doobie-core" % doobieVersion,
    "org.tpolecat" %% "doobie-hikari" % doobieVersion,
    "org.tpolecat" %% "doobie-postgres" % doobieVersion,
    "org.flywaydb" % "flyway-core" % flywayVersion,
    "ch.qos.logback" % "logback-classic" % logbackVersion,
    "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion,
    "org.ekrich" %% "sconfig" % sconfigVersion,
    "org.scalactic" %% "scalactic" % scalaTestVersion,
    "org.scalatest" %% "scalatest" % scalaTestVersion % "test",
  ) ++ Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "de.heikoseeberger" %% "akka-http-circe" % akkaHttpCirceVersion,
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
    "com.github.jwt-scala" %% "jwt-circe" % jwtCirceVersion,
    "com.github.t3hnar" %% "scala-bcrypt" % scalaBcryptVersion,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % "test"
  ).map(_.cross(CrossVersion.for3Use2_13))
}

lazy val generated = project.in(file("generated"))
  .settings(
    openApiInputSpec := s"${System.getProperty("user.dir")}/src/main/resources/api.yaml",
    openApiGeneratorName := "scala-akka",
  )

Revolver.settings