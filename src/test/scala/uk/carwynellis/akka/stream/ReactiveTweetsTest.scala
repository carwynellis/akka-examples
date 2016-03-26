package uk.carwynellis.akka.stream

import java.io.File

import org.scalatest.FunSuite

class ReactiveTweetsTest extends FunSuite {

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
}
