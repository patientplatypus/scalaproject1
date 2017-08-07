package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import JsonFormats._
/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */

case class Person(name: String, country: String, id: Int)

// I like to define a JsonFormats object which contains all of the json formats for all of my domain objects. When I
// want to read/write some json, I just import this.
object JsonFormats {
  // The two lines below create json readers and writers using a macro.
  // For it to work, you must have a reads/writes for each type making up the case class.
  // Play comes with reads/writes for String and Int, so this "just works"
  implicit val personReads = Json.reads[Person]
  implicit val personWrites = Json.writes[Person]
}

@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }
  def getName = Action {
  	Ok("Jim")
  }

  def parseperson(json: JsObject) = Action { implicit request =>
    val person = json.as[Person]
    Ok("name : " + person.name)
  }

  // implicit val placeReads: Reads[Place] = (
  //   (JsPath \ "name").read[String] and
  //   (JsPath \ "location").read[Location] and
  //   (JsPath \ "residents").read[Seq[Resident]]
  // )(Place.apply _)

}
