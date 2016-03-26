package uk.carwynellis.akka.stream

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source

/**
  * Example code from http://doc.akka.io/docs/akka/2.4.2/scala/stream/stream-quickstart.html
  */
object SimpleExample {

  implicit val system = ActorSystem("QuickStart")
  implicit val materializer = ActorMaterializer()

  def printNumbersStream(n: Int): Unit = {
    val source: Source[Int, NotUsed] = Source(1 to n)

    source runForeach(i => println(i))
  }
}
