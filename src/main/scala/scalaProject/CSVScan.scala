package scalaProject
import java.io.{BufferedOutputStream, BufferedWriter, File, FileOutputStream, FileWriter, PrintWriter}
import java.nio.file.Files
import java.text.SimpleDateFormat
import java.util.Date

import org.apache.log4j.{Level, Logger}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import net.liftweb.json._
import net.liftweb.json.Serialization.write

import scala.collection.mutable.{ArrayBuffer, ListBuffer}
import scala.util.{Failure, Success, Try}

//Cat,    name : String, race : String, age: Int
//Person, firstName: String, lastName: String, salary: Int, numberOfChildren: Int
//Car,    brand: String, countryOfBirth: String, maxSpeed: Int, speeds: Int
//Film,   mainActors: Seq[String], dateOfRelease: Date
//Actor,  name: String, filmsPlayed: Seq[String]

object CSVScan extends App{
  println(new ParseCSV().parseIt())
  println(new ParseCSV().guessParse(2))
  new ParseCSV().writeJson("/Users/lind/School/scalaProject/src/resources/hayoup.json", "/Users/lind/School/scalaProject/src/resources/hayoup.csv")
}
case class PersonCase(  firstName: String,        lastName: String,       salary: Int,    numberOfChildren: Int ) extends java.io.Serializable
case class CarCase(     brand: String,            countryOfBirth: String, maxSpeed: Int,  speeds: Int           ) extends java.io.Serializable
case class CatCase(     name : String,            race : String,          age: Int                              ) extends java.io.Serializable
case class FilmCase(    mainActors: Seq[String],  dateOfRelease: Date                                           ) extends java.io.Serializable
case class ActorCase(   name: String,             filmsPlayed: Seq[String]                                      ) extends java.io.Serializable

class ParseCSV extends java.io.Serializable{
  Logger.getLogger("org").setLevel(Level.OFF)

  val countryList = List("USA","EU")
  val formatter = new SimpleDateFormat("yyyy-MM-dd")

  val sparkSession = SparkSession.builder().master("local[2]").getOrCreate()
  val rdd = sparkSession.sparkContext.textFile("/Users/lind/School/scalaProject/src/resources/hayoup.csv")
  def parseIt() ={

    var tmpArray = ArrayBuffer.empty[Any]

    rdd.foreach{res =>
      val splt = res.split(",")
      val patternRes = splt.length match{
        case 4 if contryExists(splt(1), countryList)=>
          val theRes = CarCase(splt(0),splt(1),splt(2).toInt,splt(3).toInt)
          theRes
        case 4 if !contryExists(splt(1), countryList)=>
          val theRes = PersonCase(splt(0),splt(1),splt(2).toInt,splt(3).toInt)
          theRes
        case 2 if convertDate(splt(1), formatter)=>
          val theRes = FilmCase(splt(0).split(";").map(_.trim).toSeq, formatter.parse(splt(1)))
          theRes
        case 2 if !convertDate(splt(1), formatter)=>
          val theRes = ActorCase(splt(0), splt(1).split(";").map(_.trim).toSeq)
          theRes
        case 3 =>
          val theRes = CatCase(splt(0),splt(1),splt(2).toInt)
          theRes
        case _ =>
          println("Unhandled line")
      }
      tmpArray += patternRes
      println(patternRes)
    }
  }

  def guessParse(parseLine: Int): Any={
    val theLine = rdd.zipWithIndex().filter(_._2==parseLine).map(_._1).first()
    val objOfLine = theLine.split(",")
    val toObj = objOfLine.length match {
      case 4 if contryExists(objOfLine(1), countryList)=>
        val theCarLine = CarCase(objOfLine(0),objOfLine(1),objOfLine(2).toInt,objOfLine(3).toInt)
        theCarLine
      case 4 if !contryExists(objOfLine(1), countryList)=>
        val thePersonLine = PersonCase(objOfLine(0),objOfLine(1),objOfLine(2).toInt,objOfLine(3).toInt)
        thePersonLine
      case 2 if convertDate(objOfLine(1), formatter)=>
        val theFilmLine = FilmCase(objOfLine(0).split(";").map(_.trim).toSeq, formatter.parse(objOfLine(1)))
        theFilmLine
      case 2 if !convertDate(objOfLine(1), formatter)=>
        val theActorLine = ActorCase(objOfLine(0), objOfLine(1).split(";").map(_.trim).toSeq)
        theActorLine
      case 3 =>
        val theCatLine = CatCase(objOfLine(0),objOfLine(1),objOfLine(2).toInt)
        theCatLine
      case _ =>
        println("Unhandled line")
    }
    toObj
  }
  def writeJson(pathToJson: String, pathToCSV: String) ={
    var tmpList = ListBuffer.empty[Any]
    val rdd = sparkSession.sparkContext.textFile(pathToCSV)
    rdd.foreach{res =>
      val splitted = res.split(",")

      val patternRes = splitted.length match{
        case 4 if contryExists(splitted(1), countryList)=>
          val theRes = CarCase(splitted(0),splitted(1),splitted(2).toInt,splitted(3).toInt)
          theRes
        case 4 if !contryExists(splitted(1), countryList)=>
          val theRes = PersonCase(splitted(0),splitted(1),splitted(2).toInt,splitted(3).toInt)
          theRes
        case 2 if convertDate(splitted(1), formatter)=>
          val theRes = FilmCase(splitted(0).split(";").map(_.trim).toSeq, formatter.parse(splitted(1)))
          theRes
        case 2 if !convertDate(splitted(1), formatter)=>
          val theRes = ActorCase(splitted(0), splitted(1).split(";").map(_.trim).toSeq)
          theRes
        case 3 =>
          val theRes = CatCase(splitted(0),splitted(1),splitted(2).toInt)
          theRes
        case _ =>
          println("Unhandled line")
      }
      tmpList += patternRes
      writeInFile(pathToJson, tmpList)
    }
//    println(car)
  }
  def createCar(brand: String,countryOfBirth: String,maxSpeed: Int,speeds: Int): Car = new Car(brand,countryOfBirth,maxSpeed,speeds)
  def contryExists(contryCode: String, countryList: List[String]): Boolean = countryList.contains(contryCode)
  def convertDate(date: String, formatter: SimpleDateFormat): Boolean = Try{
    formatter.parse(date)
  }match{
    case Success(value) => true
    case Failure(exception) => false
  }


}

