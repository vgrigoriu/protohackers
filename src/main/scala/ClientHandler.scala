import java.io.{InputStream, OutputStream}
import java.net.ServerSocket
import scala.concurrent.Future

trait ClientHandler:
  def handle(input: InputStream, output: OutputStream): Unit

def runService(port: Int, handler: ClientHandler): Unit =
  val server = ServerSocket(port)
  println(s"Listening on port $port.")
  while true do
    val client = server.accept()
    Future {
      try
        handler.handle(client.getInputStream, client.getOutputStream)
      finally
        client.close()
    }