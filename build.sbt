name := "catsStudio"

version := "0.1"

scalaVersion := "2.12.4"

val catsCore = "org.typelevel" %% "cats-core" % "1.0.0-RC1"

val catsMacros= "org.typelevel" %% "cats-macros" % "1.0.0-RC1"

scalacOptions += "-Ypartial-unification"

resolvers += "Sonatype Public" at "https://oss.sonatype.org/content/groups/public/"

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)


val simulacrum = "com.github.mpilquist" %% "simulacrum" % "0.11.0"

libraryDependencies ++= Seq(
  catsCore, catsMacros, simulacrum)