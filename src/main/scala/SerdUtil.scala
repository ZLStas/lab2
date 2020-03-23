import java.io._
import java.nio.charset.StandardCharsets

import scala.util.control.NonFatal


class SerdUtil {

  def serialize(obj: Any): Array[Byte] = {
    val out = new ByteArrayOutputStream
    val os = new ObjectOutputStream(out)
    os.writeObject(obj)
    out.toByteArray
  }

  def deserialize[T](data: Array[Byte]): T = {
    val stream = new ByteArrayInputStream(data)
    val objectStream = new ObjectInputStream(stream)
    var exception: Throwable = null
    try objectStream.readObject().asInstanceOf[T]
    catch {
      case NonFatal(e) =>
        exception = e
        throw e
    } finally {
      closeAndAddSuppressed(exception, objectStream)
    }
  }

  def byteToString(bytes: Array[Byte]): String = {
    new String(bytes, StandardCharsets.UTF_8)
  }

  private def closeAndAddSuppressed(e: Throwable,
                                    resource: AutoCloseable): Unit = {
    if (e != null) {
      try {
        resource.close()
      } catch {
        case NonFatal(suppressed) =>
          e.addSuppressed(suppressed)
      }
    } else {
      resource.close()
    }
  }

}
