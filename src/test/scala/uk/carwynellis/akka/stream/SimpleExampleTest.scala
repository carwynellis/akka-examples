package uk.carwynellis.akka.stream

class SimpleExampleTest extends org.scalatest.FunSuite {

  test("print number stream should output some numbers") {
    SimpleExample.printNumbersStream(5)
  }
}
