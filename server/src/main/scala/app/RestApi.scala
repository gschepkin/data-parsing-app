package app

import akka.actor._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.util.Timeout
import app.common.IConvertTypes
import app.marshaling.{Cell, _}

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

trait RestRoutes extends StorageApi with StorageMarshaling with IConvertTypes {
  import StatusCodes._

  def fileCSV: File

  startProcess(fileCSV)

  def routes: Route = cells ~ priceForDate ~ cellForDate ~ averageForDates ~ maxMinForDates ~ cellsByDates ~ resources ~ appPage

  def cells =
    path("api" / "cells") {
      get {
        // GET /api/cells
        onSuccess(getCells()) {
          case cells: Cells => complete(OK, cells)
          case bad: BadAction => complete(OK, bad)
        }
      }
    }

  def cellsByDates =
    path("api" / "cells_by_dates" ) {
      get {
        // GET /api/cells_by_dates?start=...&end=...
        parameters("start", "end") { (start, end) =>
          onSuccess(getCellsByDates(start.toDate, end.toDate)) {
            case cells: CellsByDatesResponse => complete(OK, cells)
            case bad: BadAction => complete(OK, bad)
          }
        }
      }
    }

  def priceForDate =
    path("api" / "price_by_date" ) {
      get {
        // GET /api/price-by-date?date=...
        parameters("date") { date =>
          onSuccess(getByDate(date.toDate)) {
            case price: Price => complete(OK, price)
            case bad: BadAction => complete(OK, bad)
          }
        }
      }
    }

  def cellForDate =
    path("api" / "cell_for_date" ) {
      get {
        // GET /api/cell_by_date?date=...
        parameters("date") { date =>
          onSuccess(getCellByDate(date.toDate)) {
            case cell: Cell => complete(OK, cell)
            case bad: BadAction => complete(OK, bad)
          }
        }
      }
    }

  def averageForDates =
    path("api" / "average_price_by_dates" ) {
      get {
        // GET /api/average_price_by_dates?start=...&end=...
        parameters("start", "end") { (start, end) =>
          onSuccess(getByDates(start.toDate, end.toDate)) {
            case price: AveragePrice => complete(OK, price)
            case bad: BadAction => complete(OK, bad)
          }
        }
      }
    }

  def maxMinForDates =
    path("api" / "max_min_prices_by_dates" ) {
      get {
        // GET /api/max_min_prices_by_dates?start=...&end=...
        parameters("start", "end") { (start, end) =>
          onSuccess(getMinMaxByDates(start.toDate, end.toDate)) {
            case price: MaxMinPrices => complete(OK, price)
            case bad: BadAction => complete(OK, bad)
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
    } ~ path(Remaining) { value =>
      get {
        getFromResource("index.html")
      }
    }
}