package app

import akka.actor._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.util.Timeout
import akka.event.Logging
import app.Sever.system

import java.io.File
import scala.concurrent.ExecutionContextExecutor

/**
 * Created by schepkin on 30.01.2022.
 */

class RestApi(system: ActorSystem, timeout: Timeout, file: File) extends RestRoutes {
  implicit val requestTimeout = timeout
  implicit def executionContext: ExecutionContextExecutor = system.dispatcher

  def fileCSV: File = file

  def createStorage = system.actorOf(Storage.props, Storage.name)
}

trait RestRoutes extends StorageApi with StorageMarshaling {
  import StatusCodes._

  def fileCSV: File

  startProcess(fileCSV)

  def routes: Route = resources ~ cells ~ priceForDate ~ averageForDates ~ maxMinForDates ~ appPage

  def cells =
    path("api" / "cells") {
      get {
        // GET /api/cells
        onSuccess(getCells()) {
          case cells: Cells => complete(OK, cells)
          case failure: BadAction => complete(OK, failure)
          case _ => complete(BadRequest)
        }
      }
    }

  def priceForDate =
    path("api" / "price_by_date" ) {
      get {
        // GET /api/price-by-date
        entity(as[ByDate]) { entity =>
          onSuccess(getByDate(entity.date)) {
            case price: Price => complete(OK, price)
            case failure: BadAction => complete(OK, failure)
            case _ => complete(BadRequest)
          }
        }
      }
    }

  def averageForDates =
    path("api" / "average_price_by_dates" ) {
      get {
        // GET /api/average_price_by_dates
        entity(as[ByDates]) { entity =>
          onSuccess(getByDates(entity.start, entity.end)) {
            case price: AveragePrice => complete(OK, price)
            case failure: BadAction => complete(OK, failure)
            case _ => complete(BadRequest)
          }
        }
      }
    }

  def maxMinForDates =
    path("api" / "max_min_prices_by_dates" ) {
      get {
        // GET /api/max_min_prices_by_dates
        entity(as[MaxMinByDates]) { entity =>
          onSuccess(getMinMaxByDates(entity.start, entity.end)) {
            case price: MaxMinPrices => complete(OK, price)
            case failure: BadAction => complete(OK, failure)
            case response => complete(OK, response.toString)
          }
        }
      }
    }

  def resources =
    path("resources" / Remaining) { resource =>
      getFromResource(resource)
    }

  def appPage =
    pathSingleSlash {
      get {
        getFromResource("index.html")
      }
    } ~ path(Remaining) { _ =>
      get {
        getFromResource("index.html")
      }
    }
}