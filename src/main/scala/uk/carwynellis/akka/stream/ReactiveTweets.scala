package uk.carwynellis.akka.stream

import java.io.File

import akka.actor.ActorSystem
import akka.stream.{IOResult, ActorMaterializer}
import akka.stream.scaladsl.{Source, Framing, FileIO}
import akka.util.ByteString

import scala.concurrent.Future

/**
  * Example code from http://doc.akka.io/docs/akka/2.4.2/scala/stream/stream-quickstart.html#Time-Based_Processing
  */
object ReactiveTweets {

  final case class Author(handle: String)

  final case class Hashtag(name: String)

  final case class Tweet(author: Author, timestamp: Long, body: String) {
    def hashtags: Set[Hashtag] =
      body.split(" ").collect { case t if t.startsWith("#") => Hashtag(t) }.toSet
  }

  val akka = Hashtag("#akka")

  implicit val system = ActorSystem("QuickStart")
  implicit val materializer = ActorMaterializer()

  def tweetStreamFromFile(path: String): Source[Tweet, Future[IOResult]] =
    FileIO
      .fromFile(new File(path))
      .via(Framing.delimiter(ByteString(System.lineSeparator()), maximumFrameLength = 1024, allowTruncation = true))
      .map(_.utf8String)
      .map(_.split("\t") )
      .map {
        case Array(a: String, b: String, c: String) => Tweet(Author(a), b.toLong, c)
      }

  def extractAuthorsFromAkkaTweets(tweets: Source[Tweet, _]) =
    tweets
        .filter(_.hashtags.contains(akka))
        .map(_.author)

  def extractHashTagsFromTweets(tweets: Source[Tweet, _]) =
    tweets
      .mapConcat(_.hashtags.toList)


  def printStream(s: Source[_, _]) = s.runForeach(println)
}
