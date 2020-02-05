import java.io.{File, FileWriter, PrintWriter}

import net.liftweb.json.DefaultFormats
import net.liftweb.json.Serialization.write

import scala.collection.mutable.{ArrayBuffer, ListBuffer}

package object scalaProject {
  def writeInFile(path: String, data: ListBuffer[Any]) ={
    implicit val formats = DefaultFormats
    val theJson = write(data)
    println(theJson.toString)
    val fw = new FileWriter(path, true)
    fw.write(theJson)
    fw.close()
  }
}
