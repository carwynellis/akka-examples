package uk.carwynellis.akka.stream


import org.scalatest.FunSuite
import org.scalatest.concurrent.ScalaFutures

class ReactiveTweetsTest extends FunSuite with ScalaFutures {

  val TweetFilePath = "src/test/resources/tweets.txt"

  test("Should output single author to stdout") {
    val tweets = ReactiveTweets.tweetStreamFromFile(TweetFilePath)
    val authors = ReactiveTweets.extractAuthorsFromAkkaTweets(tweets)
    ReactiveTweets.printStream(authors)
  }

  test("Should output flattened list of hashtags") {
    val tweets = ReactiveTweets.tweetStreamFromFile(TweetFilePath)
    val hashtags = ReactiveTweets.extractHashTagsFromTweets(tweets)
    ReactiveTweets.printStream(hashtags)
  }

  test("Broadcasting tweets example should write data to two files") {
    val tweets = ReactiveTweets.tweetStreamFromFile(TweetFilePath)
    ReactiveTweets.broadcastingTweetsExample(tweets)
  }

  test("tweetCounts should return correct number of tweets") {
    val tweets = ReactiveTweets.tweetStreamFromFile(TweetFilePath)
    val f = ReactiveTweets.tweetCounts(tweets)
    whenReady(f) { result: Int => assert(result == 3) }
  }

  test("tweetCounts2 should return correct number of tweets") {
    val tweets = ReactiveTweets.tweetStreamFromFile(TweetFilePath)
    val f = ReactiveTweets.tweetCounts2(tweets)
    whenReady(f) { result: Int => assert(result == 3) }
  }
}
