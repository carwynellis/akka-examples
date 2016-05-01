package uk.carwynellis.akka.stream

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ClosedShape}
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, Merge, RunnableGraph, Sink, Source}

/**
  * Stream graph examples from http://doc.akka.io/docs/akka/2.4.4/scala/stream/stream-graphs.html
  */
object StreamGraphs extends App {

  implicit val system = ActorSystem("StreamGraphsSystem")
  implicit val materializer = ActorMaterializer()

  val closedShapeExample = RunnableGraph.fromGraph(GraphDSL.create() { implicit builder: GraphDSL.Builder[NotUsed] =>
    import GraphDSL.Implicits._
    val in = Source(1 to 10)
    // Note - you need to explicitly declare the type on the sink otherwise this will not compile.
    val out = Sink.foreach[Int](i => println(i))

    val bcast = builder.add(Broadcast[Int](2))
    val merge = builder.add(Merge[Int](2))

    val f1, f2, f3, f4 = Flow[Int].map(_ + 10)

    in ~> f1 ~> bcast ~> f2 ~> merge ~> f3 ~> out
    bcast ~> f4 ~> merge
    ClosedShape
  })

  closedShapeExample.run()
}
