name := "catsStudio"

version := "0.1"

scalaVersion := "2.12.4"
val catsVersion = "1.0.0-RC2"

val catsCore = "org.typelevel" %% "cats-core" % catsVersion

val catsMacros= "org.typelevel" %% "cats-macros" % catsVersion

val catsKernel = "org.typelevel" %% "cats-kernel" % catsVersion

val catsLaws= "org.typelevel" %% "cats-laws" % catsVersion  % Test

val catsTestKit= "org.typelevel" %% "cats-testkit" % catsVersion  % Test

val scalacheckShapeless = "com.github.alexarchambault" %% "scalacheck-shapeless_1.13" % "1.1.6" % Test

scalacOptions += "-Ypartial-unification"

resolvers += "Sonatype Public" at "https://oss.sonatype.org/content/groups/public/"

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.4")


val simulacrum = "com.github.mpilquist" %% "simulacrum" % "0.11.0"

libraryDependencies ++= Seq(
  catsCore, catsMacros, catsLaws, catsTestKit, scalacheckShapeless, simulacrum)
