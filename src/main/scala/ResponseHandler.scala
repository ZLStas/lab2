object ResponseHandler {
  val serdUtil = new SerdUtil
  def handleResponse(response: Array[Byte], scope: String): Boolean = {
    if (handleByCode(response) != true) {
      if (handlePayload(response, scope) != true) {
        return false
      }
    }
    true
  }

   def handleByCode(response: Array[Byte]): Boolean = {
    response.head match {
      case 2 => println("Ping successful!")
        true
      case 6 => println("New user, registration ok")
        true
      case 7 => println("Login ok")
        true
      case 16 => println("Message sent")
        true
      case 21 => println("File sent")
        true
      case 26 => true
      case 31 => println("No files waiting")
        true
      case 100 => println("Internal Server Error occurred. Usually that means that it’s not your fault (or you just sent something THAT bad that server was not expecting).")
        true
      case 101 => println("The transfer size is below 0 or above 10MB.")
        true
      case 102 => println("Server failed to deserilize content")
        true
      case 103 => println("Server did not understand the command.")
        true
      case 104 => println("Incorrect number or content of parameters. Server expected something different (usually the stuff described above)")
        true
      case 110 => println("The specified password is incorrect (this login was used before with another password).")
        true
      case 112 => println("This command requires login first.")
        true
      case 113 => println("Failed to send the message or file. Either receiver does not exist or his pending message quota exceeded.")
        true
      case _ => false
    }
  }

   def handlePayload(response: Array[Byte], scope: String): Boolean = {
    scope match {
      case "list" => {
        val res = serdUtil.deserialize[Array[String]](response)
        println(s"Currently logend in users are : ${res.mkString(" ")}")
        true
      }
      case "receiveMsg" => {
        val res = serdUtil.deserialize[Array[String]](response)
        println(s"The massage form user ${res.head} : ${res(1)}")
        true
      }
      case "echo" => {
        println(serdUtil.byteToString(response))
        true
      }
      case _ => println("Can't handle response from server!")
        false
    }
  }
}
