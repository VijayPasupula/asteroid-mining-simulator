val Scala3Version = "3.4.2" // Or the latest stable Scala 3 version
val ScalaTestVersion = "3.2.19" // Latest ScalaTest version

lazy val root = project
  .in(file("."))
  .settings(
    name := "AsteroidMiningSimulator",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := Scala3Version,
    scalacOptions ++= Seq(
      "-deprecation", // Emit warning and location for usages of deprecated APIs.
      "-feature", // Emit warning and location for usages of features that should be imported explicitly.
      "-unchecked", // Enable additional warnings where generated code depends on assumptions.
      "-Xlint:missing-interpolator", // Warn if a string literal is missing an interpolator and contains a '$'
      "-Ysafe-init" // Enable safe initialization checking
    ),
    libraryDependencies ++= Seq(
      // For logging:
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",
      "org.slf4j" % "slf4j-simple" % "2.0.13", // Simple logger for development

      // ScalaTest and Scalactic for testing
      "org.scalatest" %% "scalatest" % ScalaTestVersion % "test",
      "org.scalactic" %% "scalactic" % ScalaTestVersion // Not % "test" as it can be used in main code for precise equality
    )
  )