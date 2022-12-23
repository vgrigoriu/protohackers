import java.io.{InputStream, OutputStream}
import java.net.{ServerSocket, Socket}
import scala.collection.mutable.ArrayBuffer
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
      handleClient(client.getInputStream, client.getOutputStream)
    }

def handleClient(input: InputStream, output: OutputStream): Unit =
  var byte: Int = input.read()
  val received = ArrayBuffer[Byte]()
  while byte != -1 do
    output.write(byte)
    received.append(byte.toByte)
    byte = input.read()
  println(s"Closing client after receiving: ${String(received.toArray)}")
  output.close()
  input.close()