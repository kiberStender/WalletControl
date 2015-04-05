name := """WalletControl"""

version := "1.0-SNAPSHOT"

lazy val commonSettings= Seq(
  organization := "br.com.wallet",
  scalaVersion := "2.11.6"
)

lazy val root = (project in file("."))
  .settings(commonSettings: _*)
  .enablePlugins(PlayScala)

// scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  "org.postgresql" % "postgresql" % "9.4-1200-jdbc41",
  "com.github.nscala-time" %% "nscala-time" % "1.8.0"
)
