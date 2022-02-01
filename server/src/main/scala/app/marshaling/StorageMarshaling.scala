package app.marshaling

import spray.json.DefaultJsonProtocol

import java.util.Date

/**
 * Created by schepkin on 30.01.2022.
 */

sealed trait StorageResponse

case class Cell(start: Date, end: Date, price: Double) extends StorageResponse
case class Cells(start: Date, end: Date,cells: List[Cell]) extends StorageResponse
case class Price(date: Date, price: Double) extends StorageResponse
case class AveragePrice(start: Date, end: Date, price: Double) extends StorageResponse
case class MaxMinPrices(start: Date, end: Date, max: Double, min: Double) extends StorageResponse
case class BadAction(failure: String) extends StorageResponse

trait StorageMarshaling extends DefaultJsonProtocol with DateMarshaling {
  implicit val cellFormat = jsonFormat3(Cell)
  implicit val cellsFormat = jsonFormat3(Cells)
  implicit val priceFormat = jsonFormat2(Price)
  implicit val averagePriceFormat = jsonFormat3(AveragePrice)
  implicit val maxMinPricesFormat = jsonFormat4(MaxMinPrices)
  implicit val badActionFormat = jsonFormat1(BadAction)
}