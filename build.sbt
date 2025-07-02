val Scala3Version = "3.3.6" 

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
        
    )
  )