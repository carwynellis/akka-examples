package akka.examples.stream

import java.nio.file.Paths

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl._
import akka.stream.{ActorMaterializer, ClosedShape, IOResult}
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
      .fromPath(Paths.get(path))
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

  def writeAuthors = FileIO.toPath(Paths.get("/tmp/authors"))
  def writeHashtags = FileIO.toPath(Paths.get("/tmp/hashtags"))

  /**
    * Example from http://doc.akka.io/docs/akka/2.4.2/scala/stream/stream-quickstart.html#Broadcasting_a_stream
    *
    * Hacked to allow writing to files via ByteStrings.
    *
    * TODO - better way to append line separator?
    */
  def broadcastingTweetsExample(tweets: Source[Tweet, _]) = {
    val g = RunnableGraph.fromGraph(GraphDSL.create() { implicit b =>
      import GraphDSL.Implicits._

      val bcast = b.add(Broadcast[Tweet](2))
      tweets ~> bcast.in
      bcast.out(0) ~> Flow[Tweet]
        .map(_.author)
        .map((a: Author) => ByteString(a.handle + System.lineSeparator())) ~> writeAuthors
      bcast.out(1) ~> Flow[Tweet]
        .mapConcat(_.hashtags.toList)
        .map((h: Hashtag) => ByteString(h.name + System.lineSeparator())) ~> writeHashtags
      ClosedShape
    })
    g.run()
  }

  /**
    * Example from http://doc.akka.io/docs/akka/2.4.2/scala/stream/stream-quickstart.html#Materialized_values
    *
    * Obtain count of tweets from the materialized processing pipeline.
    *
    * tweetCounts illustrates what is happening in more detail than tweetCounts2 which uses a more compact syntax
    */

  val sumSink: Sink[Int, Future[Int]] = Sink.fold[Int, Int](0)(_ + _)

  def tweetCounts(tweets: Source[Tweet, _]): Future[Int] = {
    val count: Flow[Tweet, Int, NotUsed] = Flow[Tweet].map(_ => 1)

    val counterGraph: RunnableGraph[Future[Int]] =
      tweets
        .via(count)
        .toMat(sumSink)(Keep.right)

    counterGraph.run()
  }

  def tweetCounts2(tweets: Source[Tweet, _]): Future[Int] = {
    tweets.map(_ => 1).runWith(sumSink)
  }
}
