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
  val scalaTestVersion = "3.2.9"

  Seq(
    "org.typelevel" %% "cats-core" % catsVersion,
    "org.typelevel" %% "cats-kernel" % catsVersion,
    "org.typelevel" %% "cats-effect" % catsEffectVersion,
    "io.circe" %% "circe-core" % circeVersion,
    "io.circe" %% "circe-parser" % circeVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "org.tpolecat" %% "doobie-core" % doobieVersion,
    "org.tpolecat" %% "doobie-hikari"    % doobieVersion,
    "org.tpolecat" %% "doobie-postgres"  % doobieVersion,
    "org.flywaydb" % "flyway-core" % "7.11.2",
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4",
    "org.ekrich" %% "sconfig" % "1.4.4",
    "org.scalactic" %% "scalactic" % scalaTestVersion,
    "org.scalatest" %% "scalatest" % scalaTestVersion % "test",
  ) ++ Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "de.heikoseeberger" %% "akka-http-circe" % akkaHttpCirceVersion,
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
    "com.github.jwt-scala" %% "jwt-circe" % "8.0.2",
    "com.github.t3hnar" %% "scala-bcrypt" % "4.3.0",
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % "test"
  ).map(_.cross(CrossVersion.for3Use2_13))
}

Revolver.settings