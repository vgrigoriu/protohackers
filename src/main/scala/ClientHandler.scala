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
    println(s"Got client: $client.")
    Future {
      try
        handler.handle(client.getInputStream, client.getOutputStream)
      catch
        case e: Exception =>
          print("Caught exception: ")
          e.printStackTrace()
      finally
        client.close()
        println(s"Closed client $client.")
    }
