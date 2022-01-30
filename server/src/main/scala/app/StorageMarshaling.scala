package app

import spray.json.DefaultJsonProtocol
import spray.json._

import java.text.SimpleDateFormat
import java.util.Date
import scala.util.Try

/**
 * Created by schepkin on 30.01.2022.
 */

case class Cell(startDate: Date, endDate: Date, price: Double)
case class Cells(cells: List[Cell])

case class ByDate(date: Date)
case class Price(date: Date, price: Double)

case class ByDates(start: Date, end: Date)
case class AveragePrice(start: Date, end: Date, price: Double)

case class MaxMinByDates(start: Date, end: Date)
case class MaxMinPrices(start: Date, end: Date, max: Double, min: Double)

trait DateMarshaling {
  implicit object DateFormat extends JsonFormat[Date] {
    def write(date: Date) = JsString(dateToIsoString(date))
    def read(json: JsValue) = json match {
      case JsString(rawDate) =>
        parseIsoDateString(rawDate)
          .fold(deserializationError(s"Expected ISO Date format, got $rawDate"))(identity)
      case error => deserializationError(s"Expected JsString, got $error")
    }
  }

  private val localIsoDateFormatter = new ThreadLocal[SimpleDateFormat] {
    override def initialValue() = new SimpleDateFormat("yyyy-MM-dd")
  }

  private def dateToIsoString(date: Date) =
    localIsoDateFormatter.get().format(date)

  private def parseIsoDateString(date: String): Option[Date] =
    Try{ localIsoDateFormatter.get().parse(date) }.toOption
}

trait StorageMarshaling extends DefaultJsonProtocol with DateMarshaling {
  implicit val cellFormat = jsonFormat3(Cell)
  implicit val cellsFormat = jsonFormat1(Cells)
  implicit val byDateFormat = jsonFormat1(ByDate)
  implicit val priceFormat = jsonFormat2(Price)
  implicit val byDatesFormat = jsonFormat2(ByDates)
  implicit val averagePriceFormat = jsonFormat3(AveragePrice)
  implicit val maxMinByDatesFormat = jsonFormat2(MaxMinByDates)
  implicit val maxMinPricesFormat = jsonFormat4(MaxMinPrices)
}