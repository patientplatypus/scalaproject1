package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._


case class Location(lat: Double, long: Double)
case class Place(name: String, location: Location)

object Place {
  var list: List[Place] = {
    List(
      // Place(
      //   "Sandleford",
      //   Location(51.377797, -1.318965)
      // ),
      // Place(
      //   "Watership Down",
      //   Location(51.235685, -1.309197)
      // )
    )
  }
  def save(place: Place) = {
    list = list ::: List(place)
  }
}


@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }
  def getName = Action {
  	Ok("Jim")
  }

  implicit val locationReads: Reads[Location] = (
    (JsPath \ "lat").read[Double] and
    (JsPath \ "long").read[Double]
  )(Location.apply _)

  implicit val placeReads: Reads[Place] = (
    (JsPath \ "name").read[String] and
    (JsPath \ "location").read[Location]
  )(Place.apply _)

  def savePlace = Action(BodyParsers.parse.json) { request =>
    val placeResult = request.body.validate[Place]
    placeResult.fold(
      errors => {
        BadRequest(Json.obj("status" ->"KO", "message" -> JsError.toJson(errors)))
      },
      place => {
        Place.save(place)
        Ok(Json.obj("status" ->"OK", "message" -> ("Place '"+place.name+"' saved.") ))
      }
    )
  }
}





// package controllers
//
// import javax.inject._
// import play.api._
// import play.api.mvc._
// import play.api.libs.json._
// import play.api.libs.functional.syntax._
// /**
//  * This controller creates an `Action` to handle HTTP requests to the
//  * application's home page.
//  */
//
// case class Person(name: String, country: String, id: Int)
// case class Location(lat: Double, long: Double)
// case class Resident(name: String, age: Int, role: Option[String])
// case class Place(name: String, location: Location, residents: Seq[Resident])
//
//
// // I like to define a JsonFormats object which contains all of the json formats for all of my domain objects. When I
// // want to read/write some json, I just import this.
// // object JsonFormats {
// //   // The two lines below create json readers and writers using a macro.
// //   // For it to work, you must have a reads/writes for each type making up the case class.
// //   // Play comes with reads/writes for String and Int, so this "just works"
// //   implicit val personReads = Json.reads[Person]
// //   implicit val personWrites = Json.writes[Person]
// // }
//
//
// @Singleton
// class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
//
//   /**
//    * Create an Action to render an HTML page.
//    *
//    * The configuration in the `routes` file means that this method
//    * will be called when the application receives a `GET` request with
//    * a path of `/`.
//    */
//   def index() = Action { implicit request: Request[AnyContent] =>
//     Ok(views.html.index())
//   }
//   def getName = Action {
//   	Ok("Jim")
//   }
//
//   implicit val locationReads: Reads[Location] = (
//     (JsPath \ "lat").read[Double] and
//     (JsPath \ "long").read[Double]
//   )(Location.apply _)
//
//   implicit val placeReads: Reads[Place] = (
//     (JsPath \ "name").read[String] and
//     (JsPath \ "location").read[Location]
//   )(Place.apply _)
//
//   def savePlace = Action(BodyParsers.parse.json) { request =>
//     val placeResult = request.body.validate[Place]
//     placeResult.fold(
//       errors => {
//         BadRequest(Json.obj("status" ->"KO", "message" -> JsError.toJson(errors)))
//       },
//       place => {
//         Place.save(place)
//         Ok(Json.obj("status" ->"OK", "message" -> ("Place '"+place.name+"' saved.") ))
//       }
//     )
//   }
//
//
//   // def parseperson = Action(parseJson(json: JsObject)) { implicit request =>
//   //   val person = json.as[Person]
//   //   Ok("name : " + person.name)
//   // }
//
//   // implicit val placeReads: Reads[Place] = (
//   //   (JsPath \ "name").read[String] and
//   //   (JsPath \ "location").read[Location] and
//   //   (JsPath \ "residents").read[Seq[Resident]]
//   // )(Place.apply _)
//
// }
