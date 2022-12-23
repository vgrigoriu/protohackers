import Math.isPrime

class MathSuite extends munit.FunSuite:
  test("isPrime works for integers from 2 up") {
    Seq(
      (2, true),
      (3, true),
      (4, false),
      (5, true),
      (6, false),
      (31, true),
      (41, true),
      (51, false),
    ).foreach((n, prime) =>
      assertEquals(isPrime(n), prime, s"Was expecting $n to ${if prime then "" else "not "}be prime.")
    )
  }

  test("isPrime works as Protohackers checker expects for weird values") {
    Seq(
      (-1, false),
      (1, false),
      (-3, false),
    ).foreach((n, prime) =>
      assertEquals(isPrime(n), prime, s"Was expecting $n to be ${if prime then "prime" else "composite"}.")
    )
  }
