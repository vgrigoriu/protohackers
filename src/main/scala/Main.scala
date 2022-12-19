import java.net.ServerSocket

@main def hello(): Unit =
  val server = ServerSocket(1234)
  while true do
    val client = server.accept()
    val input = client.getInputStream
    val output = client.getOutputStream
    var byte: Int = input.read()
    while byte != -1 do
      output.write(byte)
      byte = input.read()
    output.close()
    input.close()
