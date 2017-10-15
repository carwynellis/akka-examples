package akka.examples.http

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer

import scala.io.StdIn.readLine

/**
  * Minimal Example from http://doc.akka.io/docs/akka/2.4.3/scala/http/routing-dsl/index.html#Minimal_Example
  */
object MinimalExample {

  implicit val system = ActorSystem("minimal-example-system")
  implicit val materializer = ActorMaterializer()
  implicit val ec = system.dispatcher

  val ServerHost = "localhost"
  val ServerPort = 8080

  val route =
    path("hello") {
      get {
        complete {
          HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>")
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


