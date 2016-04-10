package uk.carwynellis.akka.http

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
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
  //  GET resource by ID
  //  POST to collection
  //  PUT to resource ID
  //  DELETE to resource ID
  val route =
    path("users") {
      get {
        complete {
          List(User(1, "Foo Bar"), User(2, "Baz"))
        }
      }
    } ~
    path("users" / IntNumber) { userId =>
      get {
        complete {
          User(userId, "User For Requested ID")
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

case class User(id: Int, name: String)
