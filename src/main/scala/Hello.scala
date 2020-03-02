import java.text.SimpleDateFormat
import java.util.Date

import scala.io.StdIn

object Hello extends App {
  def factorial(n: Int) = {
    var factorial = 1
    for (i <- 1 to n) {
      factorial = factorial * i
    }
    factorial
  }

  println("Hello world!");
  println("Please enter your name: ");
  var name = StdIn.readLine();
  println("Hello, " + name);
  var nameLength = name.toString.length;
  println("Your name has " + nameLength + " letters");
  println(nameLength + "! = " + factorial(nameLength));
  println("Please, enter your birth date in format (DD.MM.YYYY): ");
  var birthday = StdIn.readLine();
  var dateFormat = new SimpleDateFormat("dd.MM.yyy");
  var birthDate = dateFormat.parse(birthday);
  println("Today is " + new Date() + " your birthday is " + birthDate);
}

