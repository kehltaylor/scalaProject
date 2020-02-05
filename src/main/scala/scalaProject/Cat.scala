package scalaProject

class Cat(nickname: String,raceOfCat: String,ageOfCat: Int) extends scala.Serializable {
  //Cat,    name : String, race : String, age: Int
  var name: String=nickname
  var race: String=raceOfCat
  var age: Int=ageOfCat
}
