package scalaProject

class Person(fName: String,lName: String,revenue: Int,childNum: Int) extends scala.Serializable {
  //Person, firstName: String, lastName: String, salary: Int, numberOfChildren: Int
  var firstName: String=fName
  var lastName: String=lName
  var salary: Int=revenue
  var numberOfChildren: Int=childNum
}
