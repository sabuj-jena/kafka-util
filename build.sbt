//sabuj.jena@logixal.com

lazy val akkaHttpVersion = "10.2.1"
lazy val akkaVersion    = "2.6.10"
lazy val kafkaClientVersion = "2.4.0" // current 2.6.0 not working

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization    := "com.logixal",
      scalaVersion    := "2.13.3",
      version := "0.1"
    )),
    name := "kafka-util",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http"                % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json"     % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-actor-typed"         % akkaVersion,
      "com.typesafe.akka" %% "akka-stream"              % akkaVersion,
      "ch.qos.logback"    % "logback-classic"           % "1.2.3",

      "com.typesafe.akka" %% "akka-http-testkit"        % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion     % Test,
      "org.scalatest"     %% "scalatest"                % "3.0.8"         % Test,

      "org.apache.kafka"  % "kafka-clients"             % kafkaClientVersion,
      "com.typesafe"      % "config"                    % "1.4.1"
    )
  )

