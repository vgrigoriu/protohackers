import java.io.{BufferedReader, InputStream, InputStreamReader, OutputStream}
import java.net.{Socket, SocketException}
import java.nio.charset.StandardCharsets.UTF_8
import java.util.concurrent.Executors
import scala.collection.mutable
object Challenge3 extends ClientHandler:
  override def handle(input: InputStream, output: OutputStream): Unit =
    Chat.addNewUser(input, output)

class User(input: InputStream, output: OutputStream, messageHandler: (User, String) => Boolean):
  var name: String = "UnknownStranger"

  def send(message: String): Unit =
    output.write(s"$message\n".getBytes(UTF_8))

  def receive(): Unit =
    val reader = BufferedReader(InputStreamReader(input, UTF_8))
    var line = reader.readLine()
    var goOn = true
    while line != null do
      goOn = messageHandler(this, line)
      line = if goOn then reader.readLine() else null

object Chat:
  private val executor = Executors.newSingleThreadExecutor()

  // TODO: Wrap these sets in thread-safe adapters.
  private val newUsers: mutable.Set[User] = mutable.Set()
  private val joinedUsers: mutable.Set[User] = mutable.Set()

  private def handle(user: User, message: String): Boolean =
    if isNewUser(user) then
      handleNewUser(user, message)
    else
      handleJoinedUser(user, message)

  private def isNewUser(user: User): Boolean =
    newUsers.contains(user)

  private def handleNewUser(user: User, message: String): Boolean =
    val name = message
    if !"[a-zA-Z0-9]+".r.matches(name) then
      send(user, s"Invalid username: $name")
      println(s"Trying to set invalid username: name")
      false
    else
      if joinedUsers.isEmpty then
        send(user, "* You're the only one here.")
      else
        send(user, s"* Already here: ${joinedUsers.map(_.name).mkString(", ")}")

      println(s"New user name is $message")

      val currentUsers = executor.submit(() => joinedUsers.clone()).get
      currentUsers.foreach ( send(_, s"* $name has entered the room."))

      executor.submit(() =>
        newUsers.remove(user)
        user.name = name
        joinedUsers.add(user)
      ).get
      true

  private def handleJoinedUser(user: User, message: String): Boolean =
    println(s"Got message: [${user.name}] $message")
    val currentUsers = executor.submit(() => joinedUsers.clone()).get
    currentUsers.remove(user)
    currentUsers.foreach(send(_, s"[${user.name}] $message"))
    true

  private def send(user: User, message: String): Unit =
    try
      user.send(message)
    catch
      case e: SocketException =>
        println(s"${user.name} connection closed.")
        removeUser(user)

  private def removeUser(user: User): Unit =
    val shouldAnnounce = executor.submit(() => {
      newUsers.remove(user)
      // Only announce departure if user was in joinedUsers.
      joinedUsers.remove(user)
    }).get()

    if shouldAnnounce then
      val currentUsers = executor.submit(() => joinedUsers.clone()).get
      currentUsers.foreach(send(_, s"* ${user.name} has left the room."))

  def addNewUser(input: InputStream, output: OutputStream): Unit =
    val newUser = User(input, output, handle)
    newUser.send("Welcome! How do you want us to call you?")
    executor.submit(() => newUsers.add(newUser)).get
    newUser.receive()

    // Finished receiving, remove user.
    println(s"Finished receiving from ${newUser.name}")
    removeUser(newUser)

