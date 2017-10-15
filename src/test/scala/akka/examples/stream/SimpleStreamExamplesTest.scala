package akka.examples.stream

import akka.stream.IOResult
import org.scalatest.FunSuite
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent.Future

/**
  * TODO - catch IO and assert (any IO is just sent to stdout/file at the moment)
  */
class SimpleStreamExamplesTest extends FunSuite with ScalaFutures {

  test("printNumberStream should output some numbers") {
    SimpleStreamExamples.printNumberStream(5)
  }

  test("factorial should produce expected result for 1") {
    val f: Future[BigInt] = SimpleStreamExamples.factorial(1)
    whenReady(f) { result => assert(result == BigInt(1)) }
  }

  test("factorial should produce expected result for 5") {
    val f: Future[BigInt] = SimpleStreamExamples.factorial(5)
    whenReady(f) { result => assert(result == BigInt(120)) }
  }

  test("print factorials should output factorials to n") {
    SimpleStreamExamples.printFactorials(5)
  }

  test("write factorials to file returns success") {
    val f: Future[IOResult] = SimpleStreamExamples.writeFactorialsToFile(5, "/tmp/factorials")
    whenReady(f) { result => assert(result.wasSuccessful) }
  }

  test("write factorials to file with link sink returns success") {
    val f: Future[IOResult] = SimpleStreamExamples.writeFactorialsToFile(5, "/tmp/factorials")
    whenReady(f) { result => assert(result.wasSuccessful) }
  }

  test("stream throttling example runs") {
    SimpleStreamExamples.streamThrottlingExample(5)
  }
}
