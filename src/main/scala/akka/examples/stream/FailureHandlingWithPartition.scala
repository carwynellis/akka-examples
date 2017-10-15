package akka.examples.stream

import akka.actor.ActorSystem
import akka.stream.scaladsl._
import akka.stream.{ActorMaterializer, ClosedShape}

import scala.util.{Failure, Success, Try}

// TODO - remove App trait and run via tests
//      - provide an alternative that partitions a Try() output from a preceeding stage.
//        This example only really works for side effecting functions that can fail where
//        the input needs to be retained for logging, retry etc..

/**
  * Demonstrate using a partition stage to route the success and fail values of a try
  * to different sinks.
  */
object FailureHandlingWithPartition extends App {

  implicit val system = ActorSystem("ErrorHandlingExampleSystem")
  implicit val materializer = ActorMaterializer()

  def simpleSource = Source(1 to 10)

  def monitorStage(label: String = "Monitor saw:") = Flow[Int].map{ i =>
    println(s"$label $i")
    i
  }

  def exceptionOnEven(i: Int) = {
    if (i % 2 == 0) throw new Exception("even number exception")
    else println(s"$i"); i
  }

  /**
    * Runs a given function that can fail within a partition stage.
    *
    * Successful responses are sent to output 0, failures to output 1.
    *
    * This allows failed elements to be routed to a different partial graph.
    *
    * @param f A function to run that can fail
    * @tparam A
    * @tparam B
    * @return
    */
  def errorHandlingJunction[A,B](f: A => B): Partition[A] = {

    def partitionFunction(i: A): Int = {
      Try(f(i)) match {
        case Success(_) => 0
        case Failure(ex) =>
          println(s"Caught exception: $ex")
          1
      }
    }

    Partition[A](2, partitionFunction)
  }

  /**
    * Runnable graph that consumes a finite stream of ints from 1 to 10.
    *
    * Elements are passed through a function that throws an exception on even numbers.
    *
    * The errorHandlingJunction is then used to route these exceptions to an alternate
    * partial graph.
    *
    * @return
    */
  def runFailingFunction = RunnableGraph.fromGraph(GraphDSL.create(Sink.ignore) { implicit builder =>
    sink =>
      import GraphDSL.Implicits._

      val junction = builder.add(errorHandlingJunction[Int, Int](exceptionOnEven))
      val merge = builder.add(Merge[Int](2))

      simpleSource ~> junction.in

      junction.out(0) ~> monitorStage("Odd stage got:") ~> merge.in(0)
      junction.out(1) ~> monitorStage("Even stage got:") ~> merge.in(1)

      merge.out ~> sink

      ClosedShape
  })

  runFailingFunction.run
}
