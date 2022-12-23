import scala.concurrent.ExecutionContext

given ExecutionContext = ExecutionContext.global

@main def hello(): Unit =
  runService(12345, Challenge0)
