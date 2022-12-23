import java.io.{BufferedReader, InputStream, InputStreamReader, OutputStream, OutputStreamWriter, PrintWriter}
import java.nio.charset.StandardCharsets.UTF_8
import Math.isPrime
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.json.JsonReadFeature
import com.fasterxml.jackson.databind.{DeserializationFeature, MapperFeature}
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

import scala.util.Try

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

  private def getResponse(line: String): (Boolean, String) =
    parse(line).map(request =>
      (false, s"{\"method\":\"isPrime\",\"prime\":${isPrime(request.number)}}")
    ).getOrElse((true, "WRONG!"))

case class Request(
                    @JsonProperty(required = true) method: String,
                    @JsonProperty(required = true) number: BigInt
                  )

def parse(s: String): Try[Request] =
  // {"number":927431,"method":"isPrime"}
  val mapper = JsonMapper.builder()
    .configure(MapperFeature.ALLOW_COERCION_OF_SCALARS, false)
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    .addModule(DefaultScalaModule)
    .build()
  Try { mapper.readValue(s, classOf[Request]) }.filter(_.method == "isPrime")