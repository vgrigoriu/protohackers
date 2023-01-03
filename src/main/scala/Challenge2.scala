import com.sun.rowset.internal.InsertRow

import java.io.{InputStream, OutputStream}
import java.nio.ByteBuffer
import scala.annotation.tailrec
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

object Challenge2 extends ClientHandler:
  override def handle(input: InputStream, output: OutputStream): Unit =
    val assetTracker = AssetTracker()

    var maybeBytes = fill(input, 9)
    while maybeBytes.nonEmpty do
      val message = Message(maybeBytes.get)
      println(s"Got message: $message")
      message match
        case i: Insert => assetTracker.insert(i)
        case q: Query =>
          val response = assetTracker.query(q)
          println(s"Response is: $response")
          val responseBytes = ByteBuffer.allocate(4).putInt(response)
          output.write(responseBytes.array())

      maybeBytes = fill(input, 9)

def fill(in: InputStream, howManyBytes: Int): Option[Array[Byte]] =
  val result = new Array[Byte](howManyBytes)

  @tailrec
  def fill(start: Int): Option[Array[Byte]] =
    if start == result.length then
      Some(result)
    else
      in.read(result, start, result.length - start) match
        case -1 => None
        case bytesRead => fill(start + bytesRead)

  fill(0)

sealed trait Message

object Message:
  def apply(bytes: Array[Byte]): Message =
    assert(bytes.length == 9)
    val messageType = bytes(0).toChar
    val param1 = ByteBuffer.wrap(bytes, 1, 4).getInt
    val param2 = ByteBuffer.wrap(bytes, 5, 4).getInt
    messageType match
      case 'I' => Insert(param1, param2)
      case 'Q' => Query(param1, param2)

case class Insert(timestamp: Int, price: Int) extends Message
case class Query(minTime: Int, maxTime: Int) extends Message

class AssetTracker:
  private val prices = mutable.Map[Int, Int]()

  def insert(message: Insert): Unit =
    prices.update(message.timestamp, message.price)

  def query(message: Query): Int =
    val pricesInInterval = prices.view
      .filterKeys(ts => message.minTime <= ts && ts <= message.maxTime)
      .values
      .map(BigInt(_))
      .toSeq
    if pricesInInterval.isEmpty then
      println("Asking me to compute mean of 0 numbers")
      0
    else
      (pricesInInterval.sum / pricesInInterval.length).toInt