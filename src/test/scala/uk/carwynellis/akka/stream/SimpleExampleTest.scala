package uk.carwynellis.akka.stream

import org.scalatest.FunSuite
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent.Future

class SimpleExampleTest extends FunSuite with ScalaFutures {

  test("printNumberStream should output some numbers") {
    SimpleExample.printNumberStream(5)
  }

  test("factorial should produce expected result for 1") {
    val f: Future[BigInt] = SimpleExample.factorial(1)
    whenReady(f) { result => assert(result == BigInt(1)) }
  }

  test("factorial should produce expected result for 5") {
    val f: Future[BigInt] = SimpleExample.factorial(5)
    whenReady(f) { result => assert(result == BigInt(120)) }
  }
}
