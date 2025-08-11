package backend

import cats.effect._
import cats.implicits._
import doobie.hikari.HikariTransactor
import doobie.util.ExecutionContexts
import doobie.implicits._
import doobie.ConnectionIO
import scala.concurrent.ExecutionContext

object Main extends IOApp.Simple {

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

  val program1: ConnectionIO[Int] = 42.pure[ConnectionIO] // simple ConnectionIO example

  override def run: IO[Unit] =
    transactor.use { xa =>
      for {
        res <- program1.transact(xa)
        _   <- IO.println("Backend echoing...")
        _   <- IO.println(s"Result: $res")
      } yield ()
    }
}
