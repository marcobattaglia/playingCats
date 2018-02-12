name := "catsStudio"

version := "0.1"

scalaVersion := "2.12.4"

val catsCore = "org.typelevel" %% "cats-core" % "1.0.1"

val catsMacros= "org.typelevel" %% "cats-macros" % "1.0.1"

val catsKernel = "org.typelevel" %% "cats-kernel" % "1.0.1"

val catsLaws= "org.typelevel" %% "cats-laws" % "1.0.1" % Test

val catsTestKit= "org.typelevel" %% "cats-testkit" % "1.0.1" % Test

val scalacheckShapeless = "com.github.alexarchambault" %% "scalacheck-shapeless_1.13" % "1.1.6" % Test

scalacOptions += "-Ypartial-unification"

resolvers += "Sonatype Public" at "https://oss.sonatype.org/content/groups/public/"

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.4")


val simulacrum = "com.github.mpilquist" %% "simulacrum" % "0.11.0"

libraryDependencies ++= Seq(
  catsCore, catsMacros, catsLaws, catsTestKit, scalacheckShapeless, simulacrum)
