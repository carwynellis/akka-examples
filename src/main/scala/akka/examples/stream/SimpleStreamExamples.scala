package akka.examples.stream

import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.stream.scaladsl._
import akka.stream.{ActorMaterializer, IOResult, ThrottleMode}
import akka.util.ByteString
import akka.{Done, NotUsed}

import scala.concurrent.Future
import scala.concurrent.duration._

/**
  * Example code from http://doc.akka.io/docs/akka/2.4.2/scala/stream/stream-quickstart.html
  */
object SimpleStreamExamples {

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
      .runWith(FileIO.toPath(Paths.get(path)))

  def lineSink(filename: String): Sink[String, Future[IOResult]] =
    Flow[String]
      .map(s => ByteString(s"$s\n"))
      .toMat(FileIO.toPath(Paths.get(filename)))(Keep.right)

  def writeFactorialsToFileWithLineSink(n: Int, path: String): Future[IOResult] =
    factorialSource(n).map(_.toString).runWith(lineSink(path))

  def streamThrottlingExample(n: Int): Future[Done] =
    factorialSource(n)
      .zipWith(incrementingSource(n))((num, idx) => s"$idx! = $num")
      .throttle(1, 1.second, 1, ThrottleMode.shaping)
      .runForeach(println)
}
