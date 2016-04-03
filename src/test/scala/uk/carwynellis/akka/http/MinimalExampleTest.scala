package uk.carwynellis.akka.http

import akka.http.scaladsl.model.{ContentTypes, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{FunSuite, Matchers}

/**
  * Simple test for the MinimalExample route.
  *
  * Confirms that
  *  - we get a HTTP 200 with the expected response content for GETs to /hello
  *  - requests to any other resources are rejected
  *
  * Demonstrates simple usage of ScalatestRouteTest
  */
class MinimalExampleTest extends FunSuite with ScalatestRouteTest with Matchers {

  test("should return hello message for GET of /hello resource") {
    Get("/hello") ~> MinimalExample.route ~> check {
      status should equal (StatusCodes.OK)
      responseAs[String] should include ("Say hello to akka-http")
      contentType should be (ContentTypes.`text/html(UTF-8)`)
    }
  }

  test("should return HTTP 404 for any other resources") {
    Get("/foo") ~> MinimalExample.route ~> check {
      // This request should be rejected
      handled should be (false)
    }
  }
}
