import Dependencies._

ThisBuild / scalaVersion     := "2.13.10"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

ThisBuild / scalacOptions ++= Seq(
  "-deprecation",
  "-explaintypes",
  "-feature",
  "-language:postfixOps",
  "-unchecked",
  "-encoding",
  "utf8",
  "-language:existentials",
  "-language:higherKinds",
  //"-Vimplicits",
  "-Ywarn-dead-code",
  "-Ywarn-unused:imports",
  "-Ywarn-unused:locals",
  "-Ywarn-unused:params",
  "-Ywarn-unused:patvars",
  "-Ywarn-unused:privates",
  "-Werror",
  "-Wconf:src=.*protobuf.*:info"
)

lazy val root = (project in file("."))
  .settings(
    name := "PureConfigRefined",
  )

val monocleVersion = "2.0.0"
val refinedVersion = "0.9.28"
val pureconfigVersion = "0.17.1"
val slf4jVersion = "1.7.36"
val kamonVersion = "2.1.0"
val conscryptVersion = "2.5.1" // v2.5.1 and above fixes https://github.com/grpc/grpc-java/issues/6684
val alpakkaVersion = "3.0.4"

lazy val refinedDependencies = Seq(
  "refined",
  "refined-pureconfig"
).map("eu.timepit" %% _ % refinedVersion)

lazy val pureconfigDependencies = Seq(
  "com.github.pureconfig" %% "pureconfig",
  "com.github.pureconfig" %% "pureconfig-cats",
  "com.github.pureconfig" %% "pureconfig-circe"
).map(_ % pureconfigVersion)

lazy val loggingDependencies = Seq(
  "org.slf4j" % "slf4j-api" % slf4jVersion,
  "ch.qos.logback" % "logback-classic" % "1.2.10" % Runtime,
  "net.logstash.logback" % "logstash-logback-encoder" % "4.11" % Runtime
)


libraryDependencies ++=
    refinedDependencies ++
    loggingDependencies ++
    pureconfigDependencies ++
    Seq(
      "org.typelevel" %% "cats-core" % catsVersion,
      "com.github.julien-truffaut" %% "monocle-core" % monocleVersion,
      "com.github.julien-truffaut" %% "monocle-macro" % monocleVersion,
      "org.scalatest" %% "scalatest" % scalatestVersion % Test,
      "org.scalatestplus" %% "scalacheck-1-15" % "3.2.9.0" % Test,
      "org.apache.avro" % "avro" % "1.11.0",
    )
