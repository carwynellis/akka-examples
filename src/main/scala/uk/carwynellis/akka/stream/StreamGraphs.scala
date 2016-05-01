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

  private val sumSink = Sink.fold[Int, Int](0)(_ + _)

  def main(args: Array[String]) {
    val res = Await.result(runnableGraphExample.run(), 1 second)
    println(s"Result: $res")
    system.terminate()
  }

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
}
