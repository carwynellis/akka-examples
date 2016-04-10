# getting-started-with-akka

Examples from the akka docs at http://doc.akka.io/docs/akka/2.4.2/scala/stream/stream-quickstart.html

Focusing on the streams and akka-http for now.

## akka-stream examples

  * ReactiveTweets
  * SimpleStreamExample

## akka-http examples

  * Json4sExample
    * example of Json4s based REST API supporting
        * GET     - of all resources and by ID
        * POST    - responds with location header pointing to created resource
        * PUT     - to specific ID with appropriate response for update of existing resource
        * DELETE  - of resource by ID
    * test using ScalatestRouteTest to test each HTTP method
  * MinimalExample
    * minimal route example from docs with tests
