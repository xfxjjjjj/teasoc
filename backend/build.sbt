name := "scala-backend"
version := "0.1.0"
scalaVersion := "3.6.3"

val http4sVersion = "0.23.23"

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-ember-client" % http4sVersion,
  "org.http4s" %% "http4s-ember-server" % http4sVersion,
  "org.http4s" %% "http4s-dsl"          % http4sVersion,
  "org.typelevel" %% "cats-effect"    % "3.5.1",
  "org.tpolecat"  %% "doobie-core"    % "1.0.0-RC4",
  "org.tpolecat"  %% "doobie-hikari"  % "1.0.0-RC4",
  "org.tpolecat"  %% "doobie-postgres" % "1.0.0-RC4",
  "org.postgresql" % "postgresql"     % "42.6.0"
)
