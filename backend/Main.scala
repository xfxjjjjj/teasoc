package backend

import cats.effect._
import cats.implicits._

// For creating http Routes and Server
import org.http4s._, org.http4s.dsl.io._
import org.http4s.ember.server._
import org.http4s.implicits._
import org.http4s.server.Router
import com.comcast.ip4s._

// JDBC, for handling queries
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts
import doobie.implicits._
import doobie.ConnectionIO
import scala.concurrent.ExecutionContext

object Main extends IOApp {

  // Build a transactor resource. Note the explicit type parameter [IO] on fixedThreadPool
  val transactor: Resource[IO, HikariTransactor[IO]] =
    for {
      // explicit effect type avoids ambiguous given instances
      ce <- ExecutionContexts.fixedThreadPool[IO](32) // connection EC
      xa <- HikariTransactor.newHikariTransactor[IO](
        sys.env.getOrElse("JDBC_DRIVER", "org.postgresql.Driver"),
        sys.env.getOrElse("DATABASE_URL", "jdbc:postgresql://localhost:5432/postgres"),
        sys.env.getOrElse("POSTGRES_USER", "postgres"),
        sys.env.getOrElse("POSTGRES_PASSWORD", "password"),
        ce
      )
    } yield xa

  val program1: ConnectionIO[Int] = 42.pure[ConnectionIO]

  val services = HttpRoutes.of[IO] {
    case GET -> Root / "hello" =>
      Ok(s"Hello from Backend and Database")
    case GET -> Root / "db" =>
      Ok(
        transactor.use(xa => for {
          res <- program1.transact(xa)
          out <- IO.pure(s"DataBase says: $res")
        } yield out)
      )
  }

  def run(args: List[String]): IO[ExitCode] =
    EmberServerBuilder
      .default[IO]
      .withHost(ipv4"0.0.0.0")
      .withPort(port"8081")
      .withHttpApp(services.orNotFound)
      .build
      .use(_ => IO.never)
      .as(ExitCode.Success)
}
