package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._

case class testPOST(testSTRING: String, testDOUBLE: Double)
object testPOST {
  var list: List[testPOST] = {List()}
  def save(testpost: testPOST) = {
    list = list ::: List(testpost)
  }
}

@Singleton
class Application @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  implicit val testPOSTReads: Reads[testPOST] = (
    (JsPath \ "testSTRING").read[String] and
    (JsPath \ "testDOUBLE").read[Double]
  )(testPOST.apply _)

  val testSTRINGread = (JsPath \ "testSTRING").read[String]

  val testDOUBLEread = (JsPath \ "testDOUBLE").read[Double]


  def saveTest = Action(BodyParsers.parse.json) { request =>
    val testResult = request.body.validate[testPOST]
    testResult.fold(
      errors => {
        BadRequest(Json.obj("status" ->"KO", "message" -> JsError.toJson(errors)))
      },
      testpost => {
        testPOST.save(testpost)
        Ok(Json.obj("status" ->"OK", "message" -> ("TestGET "+testpost+" saved.")))
      }
    )
  }
}
