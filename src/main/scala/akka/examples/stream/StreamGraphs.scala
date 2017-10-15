package akka.examples.stream

import akka.actor.ActorSystem
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, Merge, RunnableGraph, Sink, Source, ZipWith}
import akka.stream.{ActorMaterializer, ClosedShape, UniformFanInShape}

import scala.concurrent.Await
import scala.concurrent.duration._

import scala.language.postfixOps

/**
  * Stream graph examples from http://doc.akka.io/docs/akka/2.4.4/scala/stream/stream-graphs.html
  */
object StreamGraphs {

  implicit val system = ActorSystem("StreamGraphsSystem")
  implicit val materializer = ActorMaterializer()

  def main(args: Array[String]) {
    println("Running runnableGraphExample")
    val runnableGraphResult = Await.result(runnableGraphExample.run(), 1 second)
    println(s"  returned: $runnableGraphResult")

    println("Running parallelStreams")
    val (topF, bottomF) = parallelStreams.run()
    val topRes = Await.result(topF, 1 second)
    val bottomRes = Await.result(bottomF, 1 second)
    println(s"  Top returned: $topRes")
    println(s"  Bottom returned: $bottomRes")

    println("Running maxOfThreeRunnableGraph")
    val maxOfThreeResult = Await.result(maxOfThreeRunnableGraph.run(), 1 second)
    println(s"  returned: $maxOfThreeResult")

    system.terminate()
  }

  private val sumSink = Sink.fold[Int, Int](0)(_ + _)

  /**
    * Example runnable graph with multiple flow stages and parallel flows.
    */
  val runnableGraphExample = RunnableGraph.fromGraph(GraphDSL.create(sumSink) { implicit builder =>
    sink =>
      import GraphDSL.Implicits._

      val in = Source(1 to 10)

      val bcast = builder.add(Broadcast[Int](2))
      val merge = builder.add(Merge[Int](2))

      val f1, f2, f3, f4 = Flow[Int].map(_ + 10)

      in ~> f1 ~> bcast ~> f2 ~> merge ~> f3 ~> sink
      bcast ~> f4 ~> merge

      ClosedShape
  })

  private val topHeadSink = Sink.head[Int]
  private val bottomHeadSink = Sink.head[Int]
  private val sharedDoubler = Flow[Int].map(_ * 2)

  /**
    * Simple parallel stream example with a shared flow stage and separate sinks.
    */
  val parallelStreams = RunnableGraph.fromGraph(GraphDSL.create(topHeadSink, bottomHeadSink)((_, _)) { implicit builder =>
    (topHS, bottomHS) =>
      import GraphDSL.Implicits._

      val broadcast = builder.add(Broadcast[Int](2))

      Source.single(1) ~> broadcast.in

      broadcast.out(0) ~> sharedDoubler ~> topHS.in
      broadcast.out(1) ~> sharedDoubler ~> bottomHS.in

      ClosedShape
  })

  /**
    * Defines a fan in stage accepting 3 inputs and producing a single output.
    *
    * The output is the maximum of the three inputs.
    */
  private val pickMaxOfThree = GraphDSL.create() { implicit b =>
    import GraphDSL.Implicits._

    // Added printing to the max function to externalise what is going on
    def max(stage: Int)(a: Int, b: Int) = {
      println(s"zip$stage: determining max of ($a, $b)")
      math.max(a,b)
    }

    // Define two zip stages, which accept 2 inputs and return a single output, which will be the max of the inputs.
    val zip1 = b.add(ZipWith[Int, Int, Int](max(1)))
    val zip2 = b.add(ZipWith[Int, Int, Int](max(2)))

    // Zip2 takes the result of Zip1 as an input.
    zip1.out ~> zip2.in0

    /**
      * Combine the two zip stages so we have a shape accepting 3 inputs with 1 output which will be the max of the
      * inputs.
      *
      * More explicitly we define the following shape
      *
      * in1 ->
      *        zip1 ---->
      * in2 ->           zip2 --> out
      *               /->
      * in3 ---------/
      */
    UniformFanInShape(zip2.out, zip1.in0, zip1.in1, zip2.in1)
  }

  private val resultSink = Sink.head[Int]

  /**
    * Defines a runnable graph making use of the pickMaxOfThree stage defined above.
    */
  val maxOfThreeRunnableGraph = RunnableGraph.fromGraph(GraphDSL.create(resultSink) { implicit b =>
    sink =>
      import GraphDSL.Implicits._

      // importing the partial graph will return its shape (inlets & outlets)
      val pm3 = b.add(pickMaxOfThree)

      Source.single(1) ~> pm3.in(0)
      Source.single(2) ~> pm3.in(1)
      Source.single(3) ~> pm3.in(2)

      pm3.out ~> sink.in

      ClosedShape
  })
}
