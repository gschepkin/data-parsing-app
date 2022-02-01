import scala.language.postfixOps

ThisBuild / organization := "com.schepkin"
ThisBuild / version      := "0.1"
ThisBuild / scalaVersion := "2.13.7"

lazy val commonSettings = Seq(
  target := { baseDirectory.value / "_target" }
)

lazy val root = (project in file("."))
  .aggregate(server, client)
  .dependsOn(server, client)
  .disablePlugins(RevolverPlugin)
  .settings(
    name := "ParserApp"
  )
  
lazy val server = project
  .aggregate(client)
  .dependsOn(client)
  .enablePlugins(JavaAppPackaging)
  .settings(
    commonSettings,
    name := "Server",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor" % "2.6.18",
      "com.typesafe.akka" %% "akka-stream" % "2.6.18",
      "com.typesafe.akka" %% "akka-http" % "10.2.6",
      "com.typesafe.akka" %% "akka-http-spray-json" % "10.2.7",
      "com.lightbend.akka" %% "akka-stream-alpakka-csv" % "3.0.4",
      "com.typesafe.akka" %% "akka-stream-testkit" % "2.6.18",
      "com.typesafe.akka" %% "akka-http-testkit" % "10.2.7",
      "com.typesafe.akka" %% "akka-testkit" % "2.6.18" % Test
    )
  )

lazy val client = project
  .disablePlugins(RevolverPlugin)
  .settings(
    commonSettings,
    name := "Client",
    Compile / unmanagedSourceDirectories += baseDirectory.value / "src" / "main" / "js"
  )

Compile / run / mainClass := Some("app.Server")

Compile / mainClass := Some("app.Server")
