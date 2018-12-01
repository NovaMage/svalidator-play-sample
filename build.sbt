name := "svalidator-play-sample"

version := "2.1.0"

organization := "com.github.novamage"

description := "A play scala application showcasing the use of the svalidator-play library integration"

scalaVersion := "2.12.7"

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-explaintypes",
  "-feature",
  "-unchecked",
  "-Ywarn-dead-code",
  "-Xfatal-warnings")

lazy val root = (project in file("."))
  .enablePlugins(PlayScala, LauncherJarPlugin)


libraryDependencies += "com.github.novamage" %% "svalidator-play" % "2.1.0"

libraryDependencies += guice

libraryDependencies += "org.webjars" % "bootstrap" % "4.1.3"
