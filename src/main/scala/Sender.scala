import java.io.{DataInputStream, DataOutputStream}
import java.net.Socket

class Sender {
  var out: DataOutputStream = null
  var in: DataInputStream = null
  val serdUtil: SerdUtil = new SerdUtil()
  val EXIT_STATUS: Byte = 0
  val EXIT_CODE: Byte = 127
  val ECHO_CODE: Byte = 3
  val PING_CODE: Byte = 1
  val LOGIN_CODE: Byte = 5
  val MSG_CODE: Byte = 15
  val LIST_CODE: Byte = 10
  val RECEIVE_MSG_CODE: Byte = 25


  def this(socket: Socket) = {
    this()
    this.out = new DataOutputStream(socket.getOutputStream)
    this.in = new DataInputStream(socket.getInputStream)
  }

  def getCommand(command: String, data: Array[String] = Array[String]()): Array[Byte] = {
    command match {
      case "ping" => Array[Byte](0, 0, 0, 1, 1)
      case "echo" => {
        val message = data.mkString(" ")
        Array[Byte](0, 0, 0, (message.length + 1).toByte, ECHO_CODE) ++ message.getBytes()
      }
      case "login" => constractCommand(LOGIN_CODE, data)
      case "msg" => constractCommand(MSG_CODE, data)
      case "list" => Array[Byte](0, 0, 0, 1, LIST_CODE)
      case "receiveMsg" => Array[Byte](0, 0, 0, 1, RECEIVE_MSG_CODE)
      case "exit" => Array[Byte](EXIT_CODE)

      case _ => Array[Byte]() //invalid command
    }
  }

  def execute(command: Array[Byte]): Array[Byte] = {
    if (command.head == EXIT_CODE) {
      println("Terminating program...")
      System.exit(EXIT_STATUS)
    }
    out.write(command)
    val messageSize = in.readInt()
    val response = new Array[Byte](messageSize)
    in.readFully(response)
    response
  }

  private def constractCommand(connandConde: Byte, data: Array[String]) = {
    val sdata = serdUtil.serialize(data)
    Array[Byte](0, 0, 0, (sdata.length + 1).toByte, connandConde) ++ sdata
  }

}
