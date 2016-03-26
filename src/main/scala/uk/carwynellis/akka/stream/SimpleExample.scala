package uk.carwynellis.akka.stream

import java.io.File

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.{IOResult, ActorMaterializer}
import akka.stream.scaladsl._
import akka.util.ByteString

import scala.concurrent.Future

/**
  * Example code from http://doc.akka.io/docs/akka/2.4.2/scala/stream/stream-quickstart.html
  */
object SimpleExample {

  implicit val system = ActorSystem("QuickStart")
  implicit val materializer = ActorMaterializer()

  def incrementingSource(n: Int): Source[Int, NotUsed] = Source(1 to n)

  def printNumberStream(n: Int): Unit = {
    incrementingSource(5)
      .runForeach(i => println(i))
  }

  def factorial(n: Int): Future[BigInt] =
    incrementingSource(n)
      .runFold(BigInt(1)) { (acc, i) => acc * i }

  def factorialSource(n: Int): Source[BigInt, NotUsed] =
    incrementingSource(n)
      .scan(BigInt(1)) { (acc, i) => acc * i }

  def printFactorials(n: Int) =
    factorialSource(n)
      .runForeach(i => println(i))

  def writeFactorialsToFile(n: Int, path: String): Future[IOResult] =
    factorialSource(n)
      .map(num => ByteString(s"$num\n"))
      .runWith(FileIO.toFile(new File(path)))

  def lineSink(filename: String): Sink[String, Future[IOResult]] =
    Flow[String]
      .map(s => ByteString(s"$s\n"))
      .toMat(FileIO.toFile(new File(filename)))(Keep.right)

  def writeFactorialsToFileWithLineSink(n: Int, path: String): Future[IOResult] =
    factorialSource(n).map(_.toString).runWith(lineSink(path))



}
