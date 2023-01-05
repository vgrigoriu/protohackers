import java.io.{BufferedReader, InputStream, InputStreamReader, OutputStream}
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
      user.send(s"Invalid username: $name")
      println(s"Trying to set invalid username: name")
      false
    else
      if joinedUsers.isEmpty then
        user.send("* You're the only one here.")
      else
        user.send(s"* Already here: ${joinedUsers.map(_.name).mkString(", ")}")

      println(s"New user name is $message")
      // TODO: announce existing users.
      executor.submit(() =>
        newUsers.remove(user)
        user.name = name
        joinedUsers.add(user)
      ).get
      true

  private def handleJoinedUser(user: User, message: String): Boolean =
    // TODO: send message to other users.
    println(s"Got message: [${user.name}] $message")
    true

  def addNewUser(input: InputStream, output: OutputStream): Unit =
    val newUser = User(input, output, handle)
    executor.submit(() => newUsers.add(newUser)).get
    newUser.send("Welcome! How do you want us to call you?")
    newUser.receive()

