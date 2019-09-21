package server

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

class Router(implicit val system: ActorSystem,
             implicit val materializer: ActorMaterializer,
             implicit val executionContext: ExecutionContext) {

  private val service = new Service()

  val route =
    path("process") {
      post {
        withoutSizeLimit {
          extractDataBytes { bytes =>
            val result = service.processWords(bytes)

            onComplete(result) {
              case Success(value) => complete(value.toString)
              case Failure(exception) => complete(exception)
            }
          }
        }
      }
    }
}
