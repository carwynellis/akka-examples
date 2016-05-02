package uk.carwynellis.akka.stream

import akka.actor.ActorSystem
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, Merge, RunnableGraph, Sink, Source}
import akka.stream.{ActorMaterializer, ClosedShape}

import scala.concurrent.Await
import scala.concurrent.duration._

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

    system.terminate()
  }

  private val sumSink = Sink.fold[Int, Int](0)(_ + _)

  def runnableGraphExample = RunnableGraph.fromGraph(GraphDSL.create(sumSink) { implicit builder =>
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

  def parallelStreams = RunnableGraph.fromGraph(GraphDSL.create(topHeadSink, bottomHeadSink)((_, _)) { implicit builder =>
    (topHS, bottomHS) =>
      import GraphDSL.Implicits._

      val broadcast = builder.add(Broadcast[Int](2))

      Source.single(1) ~> broadcast.in

      broadcast.out(0) ~> sharedDoubler ~> topHS.in
      broadcast.out(1) ~> sharedDoubler ~> bottomHS.in

      ClosedShape
  })
}
