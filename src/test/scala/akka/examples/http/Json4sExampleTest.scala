package akka.examples.http

import akka.http.scaladsl.model.headers.Location
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import org.json4s.{DefaultFormats, native}
import org.scalatest.{FunSuite, Matchers}

/**
  * Tests for the Json4s example
  */
class Json4sExampleTest extends FunSuite with ScalatestRouteTest with Matchers with Json4sSupport {

  implicit val formats = DefaultFormats
  implicit val serialization = native.Serialization

  test("should return multiple users on GET of /users") {
    Get("/users") ~> Json4sExample.route ~> check {
      status should equal (StatusCodes.OK)
      contentType should be (ContentTypes.`application/json`)
      responseAs[List[User]] should be (List(User("Foo Bar"), User("Baz")))
    }
  }

  test("should return single user for GET of /users/id") {
    Get("/users/12") ~> Json4sExample.route ~> check {
      status should equal (StatusCodes.OK)
      contentType should be (ContentTypes.`application/json`)
      responseAs[User] should be (User("User For Requested ID"))
    }
  }

  test("should respond with HTTP 201 and location header pointing to created resource for POST to /users") {
    Post("/users", User("Name")) ~> Json4sExample.route ~> check {
      status should equal (StatusCodes.Created)
      headers should contain (Location(s"http://${Json4sExample.ServerHost}:${Json4sExample.ServerPort}/users/12345"))
      responseEntity should be (HttpEntity.Empty)
    }
  }

  test("should respond with HTTP 204 (assuming update of existing resource) for PUT to /users/id") {
    Put("/users/12345", User("Some User")) ~> Json4sExample.route ~> check {
      status should equal (StatusCodes.NoContent)
      responseEntity should be (HttpEntity.Empty)
    }
  }

  test("should respond with HTTP 204 for DELETE of /users/id") {
    Delete("/users/12345") ~> Json4sExample.route ~> check {
      status should equal (StatusCodes.NoContent)
      responseEntity should be (HttpEntity.Empty)
    }
  }

  test("should return HTTP 404 for any other resources") {
    Get("/foo") ~> Json4sExample.route ~> check {
      // This request should be rejected
      handled should be (false)
    }
  }
}
