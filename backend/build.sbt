name := "scala-backend"
version := "0.1.0"
scalaVersion := "3.6.3"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-effect"    % "3.5.1",
  "org.tpolecat"  %% "doobie-core"    % "1.0.0-RC4",
  "org.tpolecat"  %% "doobie-hikari"  % "1.0.0-RC4",
  "org.tpolecat"  %% "doobie-postgres" % "1.0.0-RC4",
  "org.postgresql" % "postgresql"     % "42.6.0"
)
