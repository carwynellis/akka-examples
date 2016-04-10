# getting-started-with-akka

Examples from the akka docs at http://doc.akka.io/docs/akka/2.4.2/scala/stream/stream-quickstart.html

Focusing on the streams and akka-http for now.

## akka-stream examples

  * [ReactiveTweets](src/main/scala/uk/carwynellis/akka/stream/ReactiveTweets.scala) with [tests](src/test/scala/uk/carwynellis/akka/stream/ReactiveTweetsTest.scala)
  * [SimpleStreamExample](src/main/scala/uk/carwynellis/akka/stream/SimpleStreamExample.scala) with [tests](src/test/scala/uk/carwynellis/akka/stream/SimpleExampleStreamsTest.scala)

## akka-http examples

  * [Json4sExample](src/main/scala/uk/carwynellis/akka/http/Json4sExample.scala)
    * example of Json4s based REST API supporting
        * GET     - of all resources and by ID
        * POST    - responds with location header pointing to created resource
        * PUT     - to specific ID with appropriate response for update of existing resource
        * DELETE  - of resource by ID
    * [test](src/test/scala/uk/carwynellis/akka/http/MinimalExampleTest.scala) using ScalatestRouteTest to test each HTTP method
  * [MinimalExample](src/main/scala/uk/carwynellis/akka/http/Json4sExample.scala)
    * minimal route example from docs with [tests](src/test/scala/uk/carwynellis/akka/http/MinimalExampleTest.scala)
