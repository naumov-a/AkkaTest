package server

import java.nio.charset.Charset

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import akka.util.ByteString

import scala.concurrent.{ExecutionContext, Future}

class Service(implicit val system: ActorSystem,
              implicit val materializer: ActorMaterializer,
              implicit val executionContext: ExecutionContext ) {

  private val db = new Database()

  private val mapperFilter = Flow[ByteString]
    .map( array => array.decodeString(Charset.defaultCharset()) )
    .filterNot( _ == " " )

  private val scoreObtainer = Flow[String].map( db.getScore )

  private val listBuilder = Flow[Future[Int]].fold( List.empty[Future[Int]] )( (list, future) => future :: list )

  private val reducer = Flow[List[Future[Int]]].map( list => Future.reduceLeft(list) { _ + _ } )

  def processWords(source: Source[ByteString, Any]): Future[Int] = {
    source
      .via(mapperFilter)
      .via(scoreObtainer)
      .via(listBuilder)
      .via(reducer)
      .toMat(Sink.head)(Keep.right)
      .run
      .flatten
  }
}
