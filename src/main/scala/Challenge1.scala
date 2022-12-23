import java.io.{BufferedReader, PrintWriter, InputStream, InputStreamReader, OutputStream, OutputStreamWriter}
import java.nio.charset.StandardCharsets.UTF_8
object Challenge1 extends ClientHandler:
  override def handle(input: InputStream, output: OutputStream): Unit =
    val reader = BufferedReader(InputStreamReader(input))
    val writer = PrintWriter(OutputStreamWriter(output, UTF_8), true)
    var line = reader.readLine()
    while line != null do
      println(s"got input: $line")
      writer.println(getResponse(line))
      line = reader.readLine()

  private def getResponse(line: String): String =
    // {"number":927431,"method":"isPrime"}
    val n = "-?\\d+".r.findFirstIn(line).map(_.toInt).get

    s"{\"method\":\"isPrime\",\"prime\":${isPrime(n)}}"

  private def isPrime(n: Int): Boolean =
    if n < 0 then false
    else if n == 1 then false
    else if n == 2 then true
    else if n % 2 == 0 then false
    else
      (3 to math.sqrt(n).toInt by 2).forall(n % _ != 0)