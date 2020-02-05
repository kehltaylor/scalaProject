package scalaProject

class Car (b: String,birthplace: String,maxSp: Int,spds: Int) extends scala.Serializable {
  //Car,    brand: String, countryOfBirth: String, maxSpeed: Int, speeds: Int
  var brand: String=b
  var countryOfBirth: String=birthplace
  var maxSpeed: Int=maxSp
  var speeds: Int=spds
}
