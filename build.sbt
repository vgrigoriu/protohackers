val scala3Version = "3.2.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "Protohackers",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies ++= Seq(
      "com.fasterxml.jackson.core" % "jackson-databind" % "2.14.1",
      "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.14.1",
      "org.scalameta" %% "munit" % "0.7.29" % Test,
    )
  )

ThisBuild / assemblyMergeStrategy  := {
  case PathList("module-info.class") => MergeStrategy.discard
  case x if x.endsWith("/module-info.class") => MergeStrategy.discard
  case x =>
    val oldStrategy = (ThisBuild / assemblyMergeStrategy).value
    oldStrategy(x)
}
