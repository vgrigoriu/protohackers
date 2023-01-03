import java.util.concurrent.Executors
import scala.concurrent.{ExecutionContext, Future}
import scala.io.StdIn.readLine

given ExecutionContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(10))

@main def main(): Unit =
  val services: List[ClientHandler] = List(Challenge0, Challenge1, Challenge2)
  val futures = services.zipWithIndex.map((service, i) => Future {
    runService(2000 + i, service)
  })

  readLine()
