package uk.carwynellis.akka.actor

import akka.actor.{ActorSystem, Props}
import akka.testkit.EventFilter
import com.typesafe.config.ConfigFactory
import org.scalatest.{BeforeAndAfterAll, FunSuite}

import scala.concurrent.duration._

class MyActorTest extends FunSuite with BeforeAndAfterAll {

  import uk.carwynellis.akka.actor.MyActor._

  implicit val system = ActorSystem(
      "testsystem",
      ConfigFactory.parseString("""akka.loggers = ["akka.testkit.TestEventListener"]""")
    )

  test("MyActor should log a message when receiving a Greeting message") {
    val actor = system.actorOf(Props[MyActor], "myactor-greeting")

    val eventFilter = EventFilter.info(occurrences = 1, start = "I was greeted by")

    eventFilter intercept {
      actor ! Greeting("Someone")
    }

    eventFilter assertDone Duration.Inf
  }

  test("MyActor should log a message when receiving a Goodbye message") {
    val actor = system.actorOf(Props[MyActor], "myactor-goodbye")

    val eventFilter = EventFilter.info(occurrences = 1, start = "Someone said")

    eventFilter intercept {
      actor ! Goodbye
    }

    eventFilter assertDone Duration.Inf
  }

  override def afterAll = {
    system.terminate()
  }
}
