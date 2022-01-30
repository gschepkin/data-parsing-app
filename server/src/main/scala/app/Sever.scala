package app

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.util.Timeout
import com.typesafe.config.{Config, ConfigFactory}

import java.io.File
import scala.util.{Failure, Success}

/**
 * Created by schepkin on 30.01.2022.
 */

object Sever extends App with RequestTimeout with IDivideTuple {
  val config = ConfigFactory.load()
  val host = config.getString("http.host")
  val port = config.getInt("http.port")

  implicit val system = ActorSystem("system")
  implicit val ec = system.dispatcher

  val resource = getClass.getResource("/data-20211230T0811-structure-20210419T0745.csv")
  val file = new File(resource.getPath)

  val api = new RestApi(system, requestTimeout(config), file).routes
  val bindingFuture = Http().newServerAt(host, port).bind(api)

  val log = Logging(system.eventStream, "server")

  bindingFuture.onComplete {
    case Success(_) =>
      log.info(s"RestApi bound to http://$host:$port/")
    case Failure(exception) =>
      log.error(exception, "Failed to bind to {}:{}!", host, port)
  }
}

trait RequestTimeout {
  import scala.concurrent.duration._
  def requestTimeout(config: Config): Timeout = {
    val t = config.getString("akka.http.server.request-timeout")
    val d = Duration(t)
    FiniteDuration(d.length, d.unit)
  }
}
