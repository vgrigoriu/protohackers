import java.io.{InputStream, OutputStream}
import scala.collection.mutable.ArrayBuffer

object Challenge0 extends ClientHandler:
  override def handle(input: InputStream, output: OutputStream): Unit =
    var byte: Int = input.read()
    while byte != -1 do
      output.write(byte)
      byte = input.read()
