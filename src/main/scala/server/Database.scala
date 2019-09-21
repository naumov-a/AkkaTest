package server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, Uri}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer

import scala.concurrent.{ExecutionContext, Future}

class Database(implicit val system: ActorSystem,
               implicit val materializer: ActorMaterializer,
               implicit val executionContext: ExecutionContext) {

  def getScore(word: String): Future[Int] = {
    val uri = Uri.from(scheme = "http", host = "localhost", port = 8000, path = s"/score/$word")

    Http().singleRequest(HttpRequest(uri = uri)).flatMap { result =>
      Unmarshal(result.entity).to[String].map( str => str.toInt )
    }
  }

}
