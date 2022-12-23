import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

given ExecutionContext = ExecutionContext.global

@main def main(): Unit =
  val services: List[ClientHandler] = List(Challenge0)
  val futures = services.zipWithIndex.map((service, i) => Future {
    runService(2000 + i, service)
  })

  // "Wait" for all services to "finish" (which they'll never will).
  Await.result(Future.sequence(futures), Duration.Inf)
