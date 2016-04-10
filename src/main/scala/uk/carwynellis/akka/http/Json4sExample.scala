package uk.carwynellis.akka.http

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpEntity, HttpResponse, StatusCodes}
import akka.http.scaladsl.model.headers.Location
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import org.json4s.DefaultFormats
import org.json4s.native

import scala.io.StdIn.readLine

/**
  * Simple example demonstrating Json4s based serialization / deserialization
  */
object Json4sExample extends Json4sSupport {

  implicit val system = ActorSystem("minimal-example-system")
  implicit val materializer = ActorMaterializer()
  implicit val ec = system.dispatcher

  // Json4s specific implicits
  implicit val formats = DefaultFormats
  implicit val serialization = native.Serialization

  val ServerHost = "localhost"
  val ServerPort = 8080

  // TODO - provide routes for
  //  PUT to resource ID
  //  DELETE to resource ID
  val route =
    path("users") {
      get {
        complete {
          List(User("Foo Bar"), User("Baz"))
        }
      } ~
      post {
        entity(as[User]) { user =>
          respondWithHeader(Location(s"http://$ServerHost:$ServerPort/users/12345")) {
            complete(HttpResponse(status = StatusCodes.Created, entity = HttpEntity.Empty))
          }
        }
      }
    } ~
    path("users" / IntNumber) { userId =>
      get {
        complete {
          User("User For Requested ID")
        }
      }
    }

  def main(args: Array[String]) {
    val bindingFuture = Http().bindAndHandle(route, ServerHost, ServerPort)

    // Wait for user to hit enter before shutting down the server
    readLine(s"Server online at http://$ServerHost:$ServerPort/\nPress RETURN to stop...")

    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ ⇒ system.terminate()) // and shutdown when done
  }
}

case class User(name: String)
