name := """walletControl"""

version := "1.0-SNAPSHOT"

lazy val commonSettings= Seq(
  organization := "br.com.wallet", scalaVersion := "2.11.7",
  scalacOptions := Seq("-language:_", "-deprecation", "-unchecked", "-feature", "-Xlint"),
  transitiveClassifiers in Global := Seq(Artifact.SourceClassifier),
  sources in (Compile, doc) := Nil,
  publishArtifact in (Compile, packageDoc) := false,
  parallelExecution in Test := false
)

lazy val root = (project in file("."))
  .settings(commonSettings: _*)
  .enablePlugins(PlayScala)

includeFilter in (Assets, LessKeys.less) := "*.less"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  evolutions,
  "com.typesafe.play" %% "anorm" % "2.4.0",
  "org.postgresql" % "postgresql" % "9.4-1200-jdbc41",
  "com.github.nscala-time" %% "nscala-time" % "2.2.0",
  specs2 % Test
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
