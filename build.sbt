val scala3Version = "3.1.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "lambda-scala3",
    version := "0.1.0",
    scalaVersion := scala3Version,
  )
