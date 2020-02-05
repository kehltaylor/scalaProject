package scalaProject

import java.util.Date

class Film(actors: Seq[String], releaseDate: Date) extends scala.Serializable {
  //Film,   mainActors: Seq[String], dateOfRelease: Date
  var mainActor: Seq[String] = actors
  var dateOfRelease: Date = releaseDate
}
