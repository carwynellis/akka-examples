package uk.carwynellis.akka.actor

import akka.actor.{ActorSystem, Props}
import akka.testkit.EventFilter
import com.typesafe.config.ConfigFactory
import org.scalatest.FunSuite

import scala.concurrent.duration._

class MyActorTest extends FunSuite {

  import uk.carwynellis.akka.actor.MyActor._

  implicit val system = ActorSystem(
    "testsystem",
    ConfigFactory.parseString("""akka.loggers = ["akka.testkit.TestEventListener"]""")
  )

  test("My actor should log a message when receiving a greeting") {
    try {
      val actor = system.actorOf(Props[MyActor], "myactor")

      val eventfilter = EventFilter.info(occurrences = 1, start = "I was greeted by")

      eventfilter intercept {
        actor ! Greeting("Someone")
      }

      eventfilter.assertDone(5 seconds)
    }
    finally {
      system.terminate()
    }
  }
}
