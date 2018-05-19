lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.12.6",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "json-writing-test",
    libraryDependencies ++= Seq(
      "io.argonaut" %% "argonaut" % "6.2.1",
      "io.circe" %% "circe-core" % "0.9.3")
  )
