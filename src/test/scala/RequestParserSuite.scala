import scala.util.{Failure, Success}

class RequestParserSuite extends munit.FunSuite:
  test("can parse regular request line") {
    val request = parse("{\"number\":927431,\"method\":\"isPrime\"}")

    assertEquals(request, Success(Request("isPrime", 927431)))
  }

  test("try to parse malformed lines") {
    Seq(
      "\"method\":\"isPrime\",\"number\":656029",
      "{\"number\":9537969}",
      "{\"method\":\"isPrime\",\"number\":\"1566189\"}",
      "{\"method\":\"isPrime\"}",
    ).foreach(line =>
      val request = parse(line)
      println(request)
      assertEquals(request.isFailure, true)
    )
  }

  test("valid but weird requests") {
    println(BigDecimal("168431685773709445319477572065056157462748249029384856038").toDouble)

    Seq(
      "{\"method\":\"isPrime\",\"number\":6553984.1234}",
      "{\"method\":\"isPrime\",\"not\\\"number\":\"63339213\",\"number\":1692920}",
      "{\"method\":\"isPrime\",\"number\":168431685773709445319477572065056157462748249029384856038,\"bignumber\":true}",
    ).foreach(line =>
      val request = parse(line)
      println(request)
      assertEquals(request.isSuccess, true)
    )
  }