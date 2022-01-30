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

  def routes: Route = process ~ cells ~ priceForDate ~ averageForDates ~ maxMinForDates

  val log = Logging(system.eventStream, "system")

  def process =
    path("api" / "process") {
      get {
        // GET /api/process
        onSuccess(startProcess(fileCSV)) {
          complete(OK)
        }
      }
    }

  def cells =
    path("api" / "cells") {
      get {
        // GET /api/cells
        onSuccess(getCells()) { cells =>
          complete(OK, cells)
        }
      }
    }

  def priceForDate =
    path("api" / "price_by_date" ) {
      get {
        // GET /api/price-by-date
        entity(as[ByDate]) { entity =>
          onSuccess(getByDate(entity.date)) { price =>
            complete(OK, price)
          }
        }
      }
    }

  def averageForDates =
    path("api" / "average_price_by_dates" ) {
      get {
        // GET /api/average_price_by_dates
        entity(as[ByDates]) { entity =>
          onSuccess(getByDates(entity.start, entity.end)) { price =>
            complete(OK, price)
          }
        }
      }
    }

  def maxMinForDates =
    path("api" / "max_min_prices_by_dates" ) {
      get {
        // GET /api/max_min_prices_by_dates
        entity(as[MaxMinByDates]) { entity =>
          onSuccess(getMinMaxByDates(entity.start, entity.end)) { price =>
            complete(OK, price)
          }
        }
      }
    }
}