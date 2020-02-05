package scalaProject

class Actor(actorName: String, discography: Seq[String]) extends scala.Serializable {
  //Actor,  name: String, filmsPlayed: Seq[String]
  var name: String=actorName
  var filmsPlayed: Seq[String] = discography
}
