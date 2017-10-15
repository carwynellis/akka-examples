package akka.examples.actor

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.event.Logging

/**
  * Example code from http://doc.akka.io/docs/akka/2.4.4/scala/actors.html#Defining_an_Actor_class
  */
object Main extends App {
  // ActorSystem is a heavy object: create only one per application
  val system = ActorSystem("mySystem")
  val myActor = system.actorOf(Props[MyActor], "myactor1")

  import MyActor._

  myActor ! Greeting("Someone")
  myActor ! Goodbye

  system.terminate()
}

/**
  * Companion object declares what messages the actor will receive
  */
object MyActor {
  case class Greeting(from: String)
  case object Goodbye
}

class MyActor extends Actor with ActorLogging {
  import MyActor._
  def receive = {
    case Greeting(greeter) => log.info(s"I was greeted by $greeter.")
    case Goodbye           => log.info("Someone said goodbye to me.")
  }
}




