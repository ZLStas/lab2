import java.io.IOException
import java.net.Socket
import java.util.Timer
import ResponseHandler.handleResponse

import scala.io.StdIn


object Client extends App {
  var address = args(0).split(":")
  var host = address(0)
  var port = address(1).toInt
  val socket = new Socket(host, port) //Create socket connection
  val sender = new Sender(socket)
  var credentials: Array[String] = Array[String]()

  while (credentials.isEmpty) {
    credentials = loginUser(sender)
  }

  registerReceiver(credentials, host, port)
  runMain(host, port)

  def runMain(host: String, port: Int): Unit = {
    try {

      while (true) {

        println("enter command...")
        val userInput = StdIn.readLine().split(" ")
        val comSeq = userInput.head
        val data = if (userInput.tail.isEmpty) Array[String]() else Array[String](userInput.tail.head, userInput.tail.tail.mkString(" "))
        val command = sender.getCommand(comSeq, data)

        if (command.isEmpty) {
          println("No such command!")
        } else {
          val response = sender.execute(command)
          handleResponse(response, comSeq)
        }
      }
    } catch {
      case e: IOException =>
        System.out.println("No I/O")
        System.exit(1)
      case e =>
        System.out.println("Unknown host: " + host)
        System.exit(1)
    }
  }


  private def loginUser(sender: Sender): Array[String] = {
    println("Enter login and password to log in, in one line")
    val userInput = StdIn.readLine().split(" ")
    val credentials = Array[String](userInput.head, userInput.tail.mkString)
    val loginCommand = sender.getCommand("login", credentials)
    val response = sender.execute(loginCommand)
    if (handleResponse(response, "login") == true) {
      return credentials
    }
    Array[String]()
  }

  private def registerReceiver(credentials: Array[String], host: String, port: Int) = {
    val receiver = new Receiver(host, port, credentials)
    val timer: Timer = new Timer(true)
    timer.scheduleAtFixedRate(receiver, 0, 200)
  }

}


