import java.io.{BufferedReader, PrintWriter, InputStream, InputStreamReader, OutputStream, OutputStreamWriter}
import java.nio.charset.StandardCharsets.UTF_8

import Math.isPrime

object Challenge1 extends ClientHandler:
  override def handle(input: InputStream, output: OutputStream): Unit =
    val reader = BufferedReader(InputStreamReader(input))
    val writer = PrintWriter(OutputStreamWriter(output, UTF_8), true)
    var line = reader.readLine()
    while line != null do
      println(s"got input: $line")
      val (stop, response) = getResponse(line)
      writer.println(response)
      line = if stop then null else reader.readLine()

  // FAIL:got a well-formed response to a malformed request: {"method":"isPrime","number":"3032810"}
  private def getResponse(line: String): (Boolean, String) =
    // {"number":927431,"method":"isPrime"}

    if !line.contains("{") then (true, "WRONG!")
    else
      val n = "-?\\d+".r.findFirstIn(line).map(_.toInt).get
      (false, s"{\"method\":\"isPrime\",\"prime\":${isPrime(n)}}")
