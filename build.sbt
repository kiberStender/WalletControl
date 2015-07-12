name := """WalletControl"""

version := "1.0-SNAPSHOT"

lazy val commonSettings= Seq(
  organization := "br.com.wallet",scalaVersion := "2.11.6",
  scalacOptions := Seq("-language:_", "-deprecation", "-unchecked", "-feature", "-Xlint"),
  transitiveClassifiers in Global := Seq(Artifact.SourceClassifier),
  sources in (Compile, doc) := Nil,
  publishArtifact in (Compile, packageDoc) := false,
  parallelExecution in Test := false
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
  "com.github.nscala-time" %% "nscala-time" % "2.0.0"
)
