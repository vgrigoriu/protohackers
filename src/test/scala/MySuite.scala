import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import java.nio.charset.StandardCharsets.UTF_8

// For more information on writing tests, see
// https://scalameta.org/munit/docs/getting-started.html
class MySuite extends munit.FunSuite {
  test("test Challenge0 in main with printable characters") {
    for s <- List("cucu", "", "My Little Pony!") do
      val input = ByteArrayInputStream(s.getBytes(UTF_8))
      val output = ByteArrayOutputStream()

      Challenge0.handle(input, output)

      assertEquals(output.toString(UTF_8.name()), s)
  }

  test("test Challenge0 in main with non-printable characters") {
    for bytes <- List(Array[Byte](1, 2, 3, 5)) do
      val input = ByteArrayInputStream(bytes)
      val output = ByteArrayOutputStream()

      Challenge0.handle(input, output)

      munit.Assertions.
      assertEquals(output.toByteArray.toList, bytes.toList)
  }
}
