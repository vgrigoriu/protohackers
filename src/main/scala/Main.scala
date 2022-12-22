import java.net.{ServerSocket, Socket}
import scala.concurrent.{ExecutionContext, Future}

given ExecutionContext = ExecutionContext.global

@main def hello(): Unit =
  val server = ServerSocket(12345)
  println("Bound to port 12345.")
  while true do
    println("Waiting for a connection...")
    val client = server.accept()
    println(s"... got one!: $client")
    val x = Future {
      handleClient(client)
    }

def handleClient(client: Socket): Unit =
  val input = client.getInputStream
  val output = client.getOutputStream
  var byte: Int = input.read()
  while byte != -1 do
    output.write(byte)
    byte = input.read()
  println(s"Closing client $client")
  output.close()
  input.close()