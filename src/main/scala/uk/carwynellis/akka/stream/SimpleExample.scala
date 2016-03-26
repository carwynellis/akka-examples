package uk.carwynellis.akka.stream

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source

import scala.concurrent.Future

/**
  * Example code from http://doc.akka.io/docs/akka/2.4.2/scala/stream/stream-quickstart.html
  */
object SimpleExample {

  implicit val system = ActorSystem("QuickStart")
  implicit val materializer = ActorMaterializer()

  def incrementingSource(n: Int): Source[Int, NotUsed] = Source(1 to n)

  def printNumberStream(n: Int): Unit = {
    incrementingSource(5) runForeach(i => println(i))
  }

  def factorial(n: Int): Future[BigInt] =
    incrementingSource(n).runFold(BigInt(1)) { (acc: BigInt, i: Int) => acc * BigInt(i) }

}
