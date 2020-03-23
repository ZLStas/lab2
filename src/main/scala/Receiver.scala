import java.net.Socket
import java.util.TimerTask
import ResponseHandler.handleResponse
import ResponseHandler.handleByCode

class Receiver extends TimerTask {
  var sender = new Sender
  val serdUtil = new SerdUtil
  var credentials: Array[String] = null
  var userList: Array[String] = null

  def this(host: String, port: Int, credentials: Array[String]) = {
    this()
    this.sender = new Sender(new Socket(host, port))
    println(s"Listening for incoming messages for user: ${credentials.head}")
    val loginCommand = sender.getCommand("login", credentials)
    val response = sender.execute(loginCommand)
    handleResponse(response, "login")
  }


  override def run(): Unit = {
    getMessages
    checkUsers
  }

  private def checkUsers: Unit = {
    val command = sender.getCommand("list")
    val response = sender.execute(command)
    if (handleByCode(response) == false) {
      val currentUsers = serdUtil.deserialize[Array[String]](response)
      if (userList == null) {
        userList = currentUsers
      }

      var newUsers: Array[String] = null
      if (currentUsers.length > userList.length) {
        newUsers = currentUsers.diff(userList)
        if (!newUsers.isEmpty) println(s"New user has been logged in : ${newUsers.mkString(" ")}")
      } else {
        newUsers = userList.diff(currentUsers)
        if (!newUsers.isEmpty) println(s"User has been logged out : ${newUsers.mkString(" ")}")
      }

      userList = currentUsers
    }
  }

  private def getMessages = {
    val command = sender.getCommand("receiveMsg")
    val response = sender.execute(command)
    handleResponse(response, "receiveMsg")
  }
}
