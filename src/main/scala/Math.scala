object Math:
  def isPrime(n: BigInt): Boolean =
    if n <= 1 then false
    else if n == 2 then true
    else if n % 2 == 0 then false
    else
      (3 to math.sqrt(n.doubleValue).toInt by 2).forall(n % _ != 0)
