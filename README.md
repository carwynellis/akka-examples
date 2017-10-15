# akka-examples

Various examples, mostly from the akka docs at http://doc.akka.io/docs/akka/2.4/scala.html

## akka-actor examples

 * [MyActor](src/main/scala/akka/examples/actor/MyActor.scala) with [tests](src/test/scala/uk/carwynellis/akka/actor/MyActorTest.scala)

## akka-stream examples

  * [FailureHandlingWithPartition](src/main/scala/akka/examples/stream/FailureHandlingWithPartition.scala)
  * [ReactiveTweets](src/main/scala/akka/examples/stream/ReactiveTweets.scala) with [tests](src/test/scala/uk/carwynellis/akka/stream/ReactiveTweetsTest.scala)
  * [SimpleStreamExamples](src/main/scala/akka/examples/stream/SimpleStreamExamples.scala) with [tests](src/test/scala/uk/carwynellis/akka/stream/SimpleStreamExamplesTest.scala)
  * [StreamGraphs](src/main/scala/akka/examples/stream/StreamGraphs.scala) with [tests](src/test/scala/uk/carwynellis/akka/stream/StreamGraphsTest.scala)

## akka-http REST API examples

  * [Json4sExample](src/main/scala/akka/examples/http/Json4sExample.scala)
    * example of Json4s based REST API supporting
        * GET     - of all resources and by ID
        * POST    - responds with location header pointing to created resource
        * PUT     - to specific ID with appropriate response for update of existing resource
        * DELETE  - of resource by ID
    * [tests](src/test/scala/uk/carwynellis/akka/http/Json4sExampleTest.scala) using ScalatestRouteTest to test each HTTP method
  * [MinimalExample](src/main/scala/akka/examples/http/Json4sExample.scala)
    * minimal route example from docs with [tests](src/test/scala/uk/carwynellis/akka/http/MinimalExampleTest.scala)
