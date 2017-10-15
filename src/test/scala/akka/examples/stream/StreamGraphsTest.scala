package akka.examples.stream

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{BeforeAndAfterAll, FunSuite}

class StreamGraphsTest extends FunSuite with ScalaFutures with BeforeAndAfterAll {

  implicit val system = ActorSystem("TestStreamGraphsSystem")
  implicit val materializer = ActorMaterializer()

  test("runnableGraphExample should return expected result") {
    val streamResultFuture = StreamGraphs.runnableGraphExample.run()

    whenReady(streamResultFuture) { result =>
      assert(result == 710)
    }
  }

  test("parallelStreams returns expected results") {
    val (topF, bottomF) = StreamGraphs.parallelStreams.run()

    whenReady(topF) { result =>
      assert(result == 2)
    }

    whenReady(bottomF) { result =>
      assert(result == 2)
    }
  }

  test("max of three example should return expected result") {
    val maxOfThreeFuture = StreamGraphs.maxOfThreeRunnableGraph.run()

    whenReady(maxOfThreeFuture) { result =>
      assert(result == 3)
    }
  }

  override def afterAll = system.terminate()
}
