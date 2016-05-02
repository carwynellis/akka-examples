package uk.carwynellis.akka.stream

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{BeforeAndAfterAll, FunSuite}

class StreamGraphsTest extends FunSuite with ScalaFutures with BeforeAndAfterAll {

  implicit val system = ActorSystem("TestStreamGraphsSystem")
  implicit val materializer = ActorMaterializer()

  test("runnableGraphExample should return expected result") {
    val runnableGraph = StreamGraphs.runnableGraphExample
    val streamResultFuture = runnableGraph.run()

    whenReady(streamResultFuture) { result =>
      assert(result == 710)
    }
  }

  test("parallelStreams returns expected results") {
    val parallelStreams = StreamGraphs.parallelStreams
    val (topF, bottomF) = parallelStreams.run()

    whenReady(topF) { result =>
      assert(result == 2)
    }

    whenReady(bottomF) { result =>
      assert(result == 2)
    }
  }

  override def afterAll = system.terminate()
}
